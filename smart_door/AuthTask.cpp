#include "Arduino.h"
#include "AuthTask.h"
#include "MsgService.h"
#define MIN_DIST 0.5
#define MIN_SEC 5000

extern bool auth;
extern MsgService msgService;

AuthTask::AuthTask(int trigPin, int echoPin, int ledOnPin) {
  this->trigPin = trigPin;
  this->echoPin = echoPin;
  this->ledOnPin = ledOnPin;
}

void AuthTask::init(int period) {
  Task::init(period);
  Serial.begin(9600);
  proxSensor = new Sonar(echoPin, trigPin);
  ledOn = new Led(ledOnPin);
  ledOn->switchOn();
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
        msgService.sendMsg(Msg("H"));  
      }
      break;
    case LOGIN:
      if(distance > MIN_DIST) {
        state = IDLE;
        msgService.sendMsg(Msg("F"));
      } else if(msgService.isMsgAvailable()) {
        Msg* message = msgService.receiveMsg();
        String msg = message->getContent();
        Serial.println(msg);
        state = WAITGW;
      }
      break;
    case WAITGW:
      if(distance > MIN_DIST) {
        state = IDLE;
        msgService.sendMsg(Msg("F"));
      } else if(Serial.available()) {
        char data = Serial.read();
        if(data == "O") {
          auth = true;
          state = LOGGED;
        } else if(data == "K") {
          state = IDLE;
          msgService.sendMsg(Msg("F"));
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
