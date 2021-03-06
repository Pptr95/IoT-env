#include "FlexiTimer2.h"
#include "AuthTask.h"
#include "EnvTask.h"
#include "MsgService.h"
#define TICK_TIMER 100

AuthTask *authTask = new AuthTask(7, 4, 8);   //trig, echo, ledOn
EnvTask *envTask = new EnvTask(9, 6, 5, 0, 12);   //servo, ledValue, btnExit, temp, pir
bool auth;
MsgService msgService(2, 10);   //txd(bt), rxd(bt)

void runTask() {
  authTask->tick();
  if(auth) {
    envTask->tick();
  }
}

void setup() {
  authTask->init(TICK_TIMER);
  envTask->init(TICK_TIMER);
  msgService.init();
  FlexiTimer2::set(TICK_TIMER, runTask);
  FlexiTimer2::start();
}

void loop() {
}


