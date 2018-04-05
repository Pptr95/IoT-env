#include <Servo.h>
#include "Arduino.h"
#include "EnvTask.h"
#define CLOSE_POS 0
#define OPEN_POS 180
#define MAX_DELAY 5000

unsigned long int startTime;
extern bool auth;

EnvTask::AuthTask(int servoPin, int ledValuePin, int btnExitPin, int tempPin, int pirPin){
  this->servoPin = servoPin;
  this->ledValuePin = ledValuePin;
  this->btnExitPin = btnExitPin;
  this->tempPin = tempPin;
  this->pirPin = pirPin;
  this->pir->init();
}

void EnvTask::init(int period) {
  Task::init(period);
  Serial.begin(9600);
  servoDoor.attach(servoPin);
  servoScanner.write(CLOSE_POS);
  proxSensor = new Sonar(echoPin, trigPin);
  ledOn = new Led(ledOnPin);
  ledValue = new LedExt(ledValuePin, 0);
  btnExit = new ButtonImpl(btnExitPin);
  pir = new PirImpl(pirPin);
  temp = new TempSensor(tempPin);
  state = IDLE;
}

void EnvTask::tick() {
  switch(state) {
    case IDLE:
      if(auth) {
        state = DETECTING;
        servoDoor.write(OPEN_POS);
        startTime = millis();
      }
     break;
    case DETECTING:
      if((millis() - startTime) >= MAX_DELAY) {
        //invio F al bt
        Serial.println("F");
        servoScanner.write(OPEN_POS);
        auth = false;
        state = IDLE;
      } else if((millis() - startTime) < MAX_DELAY && pir->isDetected()) {
        Serial.println("Y");
        state = WORKING;
      }
      break;
    case WORKING:
      if(Serial.available()) {    //read value from bt
        int brightness = Serial.read();
        ledValue->setIntensity(brightness);
      }
      float temperature = temp->readTemperature();
      
      Serial.println();
      Serial.println("F");
      break;
  }
}
