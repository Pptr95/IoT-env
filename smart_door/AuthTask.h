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
    int servoPin;
    int trigPin;
    int echoPin;
    int ledOnPin;
    int ledValuePin;
    int btnExitPin;
    int tempPin;
    int rxdPin;
    int txdPin;
    int pirPin;
    int servoPos;
    Servo servoDoor;
    Light* ledOn;
    LightExt* ledValue;
    ButtonImpl* btnExit;
    PirImpl* pir;
    TempSensor* temp;
    Sonar* proxSensor;
    enum {IDLE, INCOMING, LOGIN, DETECTING, WORKING} state;
    void boot();
    
  public:
    AuthTask(int servoPin, int trigPin, int echoPin, int ledOnPin, int ledValuePin, int btnExitPin, int tempPin, int rxdPin, int txdPin, int pirPin);
    void init(int period);
    void tick();
};

#endif
