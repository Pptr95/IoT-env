#include "Arduino.h"
#include "AuthTask.h"
#define MIN_DIST 0.5
#define MIN_SEC 5000

float distance;
unsigned long int startTime;
extern bool auth;

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
  state = IDLE;
}

void AuthTask::tick() {
  distance = proxSensor->getDistance();
  switch(state) {
    case IDLE:
      if(distance <= MIN_DIST) {
        state = INCOMING;
        startTime = millis();
      }
      break;
    case INCOMING:
      if(distance > MIN_DIST) {
        state = IDLE;
      } else if((millis() - startTime) >= MIN_SEC){
        state = LOGIN;
        Serial.println("H");    //bt
      }
      break;
    case LOGIN:
      if(distance > MIN_DIST) {
        state = IDLE;
        Serial.println("F");    //bt
      } else if(Serial.available()) {   //from bt
        //char data = Serial.read();
        //Serial.println(data);
        state = WAITGW;
      }
      break;
    case WAITGW:
      if(distance > MIN_DIST) {
        state = IDLE;
        Serial.println("F");    //bt
      } else if(Serial.available()) {
        char data = Serial.read();
        if(data == "O") {
          auth = true;
          state = LOGGED;
        } else if(data == "K") {
          state = IDLE;
          Serial.println("F");  //bt
        }
      }
      break;
    case LOGGED:
      if(!auth) {
        state = IDLE;
      }
      break;
  }
}
