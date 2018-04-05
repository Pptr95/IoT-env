#include "FlexiTimer2.h"
#include "AuthTask.h"
#include "EnvTask.h"
#define OMEGA 100

AuthTask *authTask = new AuthTask(7, 4, 6, 2, 10);   //trig, echo, ledOn,  rxd(bt), txd(bt)
EnvTask *envTask = new EnvTask(9, 8, 5, 0, 12);   //servo, ledValue, btnExit, temp, pir
bool auth;

void runTask() {
  authTask->tick();
  if(auth) {
    envTask->tick();
  }
}

void setup() {
  authTask->init(OMEGA);
  FlexiTimer2::set(OMEGA, runTask);
  FlexiTimer2::start();
}

void loop() {
}

