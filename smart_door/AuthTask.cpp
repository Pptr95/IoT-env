#include "Arduino.h"
#include "AuthTask.h"
#define MIN_DIST 0.5
#define MIN_SEC 5000

float distance;
unsigned long int startTime;

AuthTask::AuthTask(int trigPin, int echoPin, int ledOnPin, int rxdPin, int txdPin) {
  this->trigPin = trigPin;
  this->echoPin = echoPin;
  this->ledOnPin = ledOnPin;
  this->rxdPin = rxdPin;
  this->txdPin = txdPin;
}

void AuthTask::init(int period) {
  Task::init(period);
  Serial.begin(9600);
  proxSensor = new Sonar(echoPin, trigPin);
  ledOn = new Led(ledOnPin);
}

void AuthTask::tick() {
  distance = proxSensor->getDistance();
  if(state != IDLE && state != WORKING && distance > MIN_DIST) {
    //TODO
  }
  switch(state) {
    case IDLE:
      if(distance <= MIN_DIST) {
        state = INCOMING;
        startTime = millis();
      }
      break;
    case INCOMING:
      if((millis() - startTime) >= MIN_SEC){
        state = LOGIN;
        //send hello to bt
      }
      break;
    case LOGIN:
      //wait login data from bt
      break;
    case WAITGW:
      //wait response from gw
      if(result == 'O') { //OK, result is the gw response
        servoDoor.write(180);
      } else if(result == 'F') { // FAIL
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
