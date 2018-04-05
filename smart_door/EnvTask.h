#ifndef __ENVTASK__
#define __ENVTASK__

#include <Servo.h>
#include "Task.h"
#include "MsgService.h"
#include "LedExt.h"
#include "ButtonImpl.h"
#include "TempSensor.h"
#include "SoftwareSerial.h"
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
    enum {IDLE, DETECTING, WORKING} state;
    
  public:
    AuthTask(int servoPin, int ledValuePin, int btnExitPin, int tempPin, int pirPin);
    void init(int period);
    void tick();
};

#endif
