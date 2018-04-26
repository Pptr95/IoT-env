#ifndef __ENVTASK__
#define __ENVTASK__

#include <Servo.h>
#include "Task.h"
#include "LedExt.h"
#include "ButtonImpl.h"
#include "TempSensor.h"
#include "PirImpl.h"

class EnvTask: public Task {

  private:
    int servoPin;
    int ledValuePin;
    int btnExitPin;
    int tempPin;
    int pirPin;
    int servoPos;
    Servo servoDoor;
    LightExt* ledValue;
    ButtonImpl* btnExit;
    PirImpl* pir;
    TempSensor* temp;
    unsigned long int startTime;
    enum {IDLE, DETECTING, WORKING} state;
    
  public:
    EnvTask(int servoPin, int ledValuePin, int btnExitPin, int tempPin, int pirPin);
    void init(int period);
    void logout();
    void tick();
};

#endif
