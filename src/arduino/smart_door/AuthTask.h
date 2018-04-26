#ifndef __AUTHTASK__
#define __AUTHTASK__

#include "Task.h"
#include "Led.h"
#include "Sonar.h"

class AuthTask: public Task {

  private:
    int trigPin;
    int echoPin;
    int ledOnPin;
    Light* ledOn;
    Sonar* proxSensor;
    unsigned long int startTime;
    float distance;
    enum {IDLE, INCOMING, LOGIN, WAITGW, LOGGED} state;
    
  public:
    AuthTask(int trigPin, int echoPin, int ledOnPin);
    void init(int period);
    void tick();
};

#endif
