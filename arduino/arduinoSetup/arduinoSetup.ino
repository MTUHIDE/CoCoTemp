
#include <DS3231.h> //clock
#include <EEPROM.h>

void setup() {
  
  //SET THE INITIAL TIME HERE-
  // Manual (Year, Month, Day, Hour, Minute, Second)
  
  DS3231 clock;
  clock.begin();
  clock.setDateTime(2016, 10, 25, 19, 04, 00);
  
  //Set up the device ID in eeprom
  eepromSetup();
}

//save the device id to the eeprom 
//device id is 4 bytes long
//We can put this in its own sketch and run it in initial startup
void eepromSetup(){
  
  byte DEVICEID[] = {0x12,0x34, 0x56, 0x78};
  int addr = 0;

  //write the device id one byte at a time to 
  //the eeprom
  for (;addr < 4;addr++){
    if(addr == EEPROM.length())
      addr = 0;
     EEPROM.write(addr,DEVICEID[addr]);
  }
}

void loop(){
  
}

