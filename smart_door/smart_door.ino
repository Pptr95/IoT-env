#include "FlexiTimer2.h"
#include "AuthTask.h"
#include "EnvTask.h"
#include "MsgService.h"
#define OMEGA 100

AuthTask *authTask = new AuthTask(7, 4, 6);   //trig, echo, ledOn
EnvTask *envTask = new EnvTask(9, 8, 5, 0, 12);   //servo, ledValue, btnExit, temp, pir
bool auth;
MsgService msgService(2, 10);   //txd(bt), rxd(bt)

void runTask() {
  authTask->tick();
  if(auth) {
    envTask->tick();
  }
}

void setup() {
  authTask->init(OMEGA);
  envTask->init(OMEGA);
  msgService.init();
  FlexiTimer2::set(OMEGA, runTask);
  FlexiTimer2::start();
}

void loop() {
}

