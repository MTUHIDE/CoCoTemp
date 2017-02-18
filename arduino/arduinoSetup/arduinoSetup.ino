
#include <DS3231.h> //clock

void setup() {
  
  //SET THE INITIAL TIME HERE-
  // Manual (Year, Month, Day, Hour, Minute, Second)
  
  DS3231 clock;
  clock.begin();
  clock.setDateTime(2016, 11, 25, 12, 44, 00);
  
}

void loop(){
  
}

