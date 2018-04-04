#include "FlexiTimer2.h"
#include "AuthTask.h"

#define OMEGA 100

AuthTask *authTask = new AuthTask(9, 7, 4, 6, 8, 4, 0, 2, 10, 12);   //servo, trig, echo, ledOn, ledValue, btnExit, temp, rxd(bt), txd(bt), pir

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
