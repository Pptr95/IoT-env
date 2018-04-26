#ifndef __PIR__
#define __Pir__

#include "Pir.h"

class PirImpl: public Pir { 
public:
  PirImpl(int pin);
  bool isDetected();
  void init();
private:
  int pin;  
};

#endif
