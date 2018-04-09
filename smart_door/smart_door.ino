#include "FlexiTimer2.h"
#include "AuthTask.h"
#include "EnvTask.h"
#include "MsgService.h"
#include "SoftwareSerial.h"
#define OMEGA 100

AuthTask *authTask = new AuthTask(7, 4, 6);   //trig, echo, ledOn
EnvTask *envTask = new EnvTask(9, 8, 5, 0, 12);   //servo, ledValue, btnExit, temp, pir
bool auth;
MsgService msgService(10, 2);   //rxd(bt), txd(bt)

void runTask() {
  authTask->tick();
  if(auth) {
    envTask->tick();
  }
}

void setup() {
  authTask->init(OMEGA);
  envTask->init(OMEGA);
  FlexiTimer2::set(OMEGA, runTask);
  FlexiTimer2::start();
}

void loop() {
}

