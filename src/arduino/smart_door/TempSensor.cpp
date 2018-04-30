#include "TempSensor.h"
#include "Arduino.h"

TempSensor::TempSensor(int pin) {
  this->pin = pin;
  pinMode(pin, INPUT);     
} 

int TempSensor::readTemperature() {
  int temp = analogRead(pin);
  int tempC = temp * 0.48875;
  return tempC;
}

