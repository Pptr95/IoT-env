#include <Servo.h>
#include "Arduino.h"
#include "EnvTask.h"
#include "MsgService.h"
#include "SoftwareSerial.h"
#define CLOSE_POS 20
#define OPEN_POS 160
#define MAX_DELAY 5000
#define SEND_DELAY 5000

extern bool auth;
extern MsgService msgService;

EnvTask::EnvTask(int servoPin, int ledValuePin, int btnExitPin, int tempPin, int pirPin) {
  this->servoPin = servoPin;
  this->ledValuePin = ledValuePin;
  this->btnExitPin = btnExitPin;
  this->tempPin = tempPin;
  this->pirPin = pirPin;
}

void EnvTask::init(int period) {
  Task::init(period);
  Serial.begin(9600);
  servoDoor.attach(servoPin);
  servoDoor.write(CLOSE_POS);
  ledValue = new LedExt(ledValuePin, 0);
  btnExit = new ButtonImpl(btnExitPin);
  pir = new PirImpl(pirPin);
  pir->init();
  temp = new TempSensor(tempPin);
  state = IDLE;
}

void EnvTask::logout() {
    msgService.sendMsg(Msg("L"));
    Serial.println("L");
    servoDoor.write(CLOSE_POS);
    ledValue->setIntensity(0);
    ledValue->switchOff();
    auth = false;
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
        msgService.sendMsg(Msg("P")); //send fail to bt
        Serial.println("P"); // send fail to GW
        servoDoor.write(CLOSE_POS);
        auth = false;
        state = IDLE;
      } else if(((millis() - startTime) < MAX_DELAY) && pir->isDetected()) {
        Serial.println("Y");
        msgService.sendMsg(Msg("Y"));
        ledValue->switchOn();
        state = WORKING;
        startTime = millis();
      }
      break;
    case WORKING:
      String msg;
      if(msgService.isMsgAvailable()) {
        Msg* message = msgService.receiveMsg();
        msg = message->getContent();
        if(msg != "L") {
          int intensity = msg.toInt();
          ledValue->setIntensity(intensity);
        } else { //else means that msg is "L"
          logout();
        }
       }
      float temperature = temp->readTemperature();
      int ledIntensity = ledValue->getIntensity();
      if((millis() - startTime) >= SEND_DELAY) {
        Serial.print("[");
        Serial.print(temperature);
        Serial.print(" ");
        Serial.print(ledIntensity);
        Serial.println("]");
        msgService.sendMsg(Msg(String(temperature)));
        startTime = millis();
      }
      if(btnExit->isPressed()) {
        logout();
      }
      break;
  }
}

