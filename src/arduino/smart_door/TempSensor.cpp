#include "TempSensor.h"
#include "Arduino.h"

TempSensor::TempSensor(int pin) {
  this->pin = pin;
  pinMode(pin, INPUT);     
} 

float TempSensor::readTemperature() {
  int temp = analogRead(pin);
  float tempC = temp * 0.48875;
  return tempC;
}

