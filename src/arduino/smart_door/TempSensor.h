#ifndef __TEMP_SENSOR__
#define __TEMP_SENSOR__

class TempSensor {

 public: 
  TempSensor(int pin);
  int readTemperature();

private:
  int pin;
};

#endif


