#include "TempSensor.h"

TempSensor::TempSensor() {
  this->pin = pin;
  pinMode(pin, INPUT);     
} 

float TempSensor::readTemperature() {
  int temp = analogRead(pin);
  float tempC = temp * 0.48875;
  return tempC;
}
