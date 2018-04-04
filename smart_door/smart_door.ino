#include "FlexiTimer2.h"
#include "AuthTask.h"
#include "EnvTask.h"
#define OMEGA 100
//AuthTask *authTask = new AuthTask(9, 7, 4, 6, 8, 4, 0, 2, 10, 12);   //servo, trig, echo, ledOn, ledValue, btnExit, temp, rxd(bt), txd(bt), pir
AuthTask *authTask = new AuthTask(7, 4, 6, 2, 10);   //trig, echo, ledOn,  rxd(bt), txd(bt)
EnvTask *envTask = new EnvTask(9, 8, 4, 0, 12);   //servo, ledValue, btnExit, temp, pir


void runTask() {
  authTask->tick();
}

void setup() {
  authTask->init(OMEGA);
  FlexiTimer2::set(OMEGA, runTask);
  FlexiTimer2::start();
}

void loop() {
}

