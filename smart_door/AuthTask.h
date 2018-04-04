#ifndef __AUTHTASK__
#define __AUTHTASK__

#include <Servo.h>
#include "Task.h"
#include "MsgService.h"
#include "Led.h"
#include "LedExt.h"
#include "ButtonImpl.h"
#include "TempSensor.h"
#include "SoftwareSerial.h"
#include "Sonar.h"
#include "PirImpl.h"

class AuthTask: public Task {

  private:
    int trigPin;
    int echoPin;
    int ledOnPin;
    int rxdPin;
    int txdPin;
    Light* ledOn;
    Sonar* proxSensor;
    enum {IDLE, INCOMING, LOGIN, WAITGW, LOGGED} state;
    
  public:
    AuthTask(int trigPin, int echoPin, int ledOnPin, int rxdPin, int txdPin);
    void init(int period);
    void tick();
};

#endif
