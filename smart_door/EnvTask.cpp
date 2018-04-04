#include <Servo.h>
#include "Arduino.h"
#include "EnvTask.h"
#define INIT_POS 0
#define MIN_DIST 0.5
#define MIN_SEC 5000

float distance;
unsigned long int startTime;

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
  proxSensor = new Sonar(echoPin, trigPin);
  ledOn = new Led(ledOnPin);
  ledValue = new LedExt(ledValuePin, 0);
  btnExit = new ButtonImpl(btnExitPin);
  pir = new PirImpl(pirPin);
  temp = new TempSensor(tempPin);
}



void EnvTask::tick() {
  switch(state) {
    case IDLE:

     break;
    case DETECTING:
    
      break;
    case WORKING:
    
      break;
  }
}
