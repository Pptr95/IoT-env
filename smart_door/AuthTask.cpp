#include <Servo.h>
#include "Arduino.h"
#include "AuthTask.h"
#define INIT_POS 0
#define MIN_DIST 0.5
#define MIN_SEC 5000

float distance;
unsigned long int startTime;

AuthTask::AuthTask(int servoPin, int trigPin, int echoPin, int ledOnPin, int ledValuePin, int btnExitPin, int tempPin, int rxdPin, int txdPin, int pirPin) {
  this->servoPin = servoPin;
  this->trigPin = trigPin;
  this->echoPin = echoPin;
  this->ledOnPin = ledOnPin;
  this->ledValuePin = ledValuePin;
  this->btnExitPin = btnExitPin;
  this->tempPin = tempPin;
  this->rxdPin = rxdPin;
  this->txdPin = txdPin;
  this->pirPin = pirPin;
  this->pir->init();
}

void AuthTask::init(int period) {
  Task::init(period);
  Serial.begin(9600);
  servoDoor.attach(servoPin);
  proxSensor = new Sonar(echoPin, trigPin);
  ledOn = new Led(ledOnPin);
  ledValue = new LedExt(ledValuePin, 0);
  btnExit = new ButtonImpl(btnExitPin);
  pir = new PirImpl(pirPin);
  temp = new TempSensor(tempPin);
  boot();
}

void RadarTask::boot() {
  servoPos = INIT_POS;
  servoScanner.write(servoPos);
  ledOn->switchOn();
  
  state = IDLE;
}

void RadarTask::tick() {
  distance = proxSensor->getDistance();
  if(state != IDLE && distance > MIN_DIST) {
    servoDoor.write(INIT_POS);
    state = IDLE;
  }
  switch(state) {
    case IDLE:
      if(distance <= MIN_DIST) {
        state = INCOMING;
        startTime = millis();
      }
      break;
    case INCOMING:
      if((millis() - startTime) > MIN_SEC){
        state = LOGIN;
        //send hello to bt
      }
      break;
    case LOGIN:
      //wait login data from bt
      break;
    case WAITGW:
      //wait response from gw
      if(result == 'O') {
        servoDoor.write(180);
      } else if(result == 'F') {
        //send failed login to bt
        state = IDLE;
      }
      break;
    case DETECTING:
    
      break;
    case WORKING:
    
      break;
  }
}
