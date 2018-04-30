#include "PirImpl.h"
#include "Arduino.h"

PirImpl::PirImpl(int pin){
  this->pin = pin;
}

void PirImpl::init(){
   digitalWrite(pin, LOW);
   pinMode(pin, INPUT);
}

bool PirImpl::isDetected(){
  if(digitalRead(pin) == HIGH) {
    return true;
  } else {
    return false;
  }
};

