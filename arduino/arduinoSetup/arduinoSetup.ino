
#include <DS3231.h> //clock

void setup() {
  
  //SET THE INITIAL TIME HERE-
  // Manual (Year, Month, Day, Hour, Minute, Second)
  
  DS3231 clock;
  clock.begin();
  clock.setDateTime(2017, 3, 16, 22, 12, 30);
  
}

void loop(){
  
}

