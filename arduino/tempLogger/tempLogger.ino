

/*
  This code logs the temperature from a DS18B20 temperature sensor
  and prints the temperature along with a time stamp using a
  DS3231 Clock and a SparkFun microSD Transflash Breakout

  This uses code from http://bildr.org/2011/07/ds18b20-arduino/
   and https://learn.sparkfun.com/tutorials/microsd-shield-and-sd-breakout-hookup-guide sd card code
   and http://tronixstuff.com/2014/12/01/tutorial-using-ds1307-and-ds3231-real-time-clock-modules-with-arduino/ using the clock
   and https://github.com/OSBSS/TRH/blob/master/TRH.ino main code body
   and https://github.com/jarzebski/Arduino-DS3231 library we use for clock interrupts
   and was modified to save the temperature, in fahrenheit, and time data to a microSD card

  @author Humane Interface Design Enterprise, Michigan Tech
    -Stephen Radachy
    -Nicholas Lanter

    NOTES:
    1) AFTER SETTING THE TIME COMMENT IT OUT
       OTHERWISE THE CLOCK IS SET TO THAT DATE EVERY
       TIME YOU BOOT UP
    
    2) The processor doesn't begin logging until the
       minute it is on finishes but then it consistently
       runs from there, so if it starts at 10:01:29 it waits
       until 10:02:00 to begin
    
    3) On power up, the temperature sensor sends 185 deg F to verify booting
       This code omits that initial log, so it may actually begin logging after
       up to one plus timeToSleep minutes
    
    4) Currently, the processor wakes up every minute to see if the specified 
       timeToSleep minutes have passed, and then it logs when true. This is due
       to the nature of the RTC's alarm, and with some math it can be configured 
       to wake up only once every timeToSleep minutes
*/

#include <OneWire.h> //temp sensor
#include <SPI.h>
#include "SdFat.h" //sd card
SdFat SD;
#include "Wire.h"
#include <avr/sleep.h>  //sleep library
#include <DS3231.h> //clock
#include <EEPROM.h>

#define DEBUG 1
#define DEBUG_TO_SD 1
#define POWA 4    // pin 4 supplies power to microSD card breakout and temp sensor

//Clock var's
#define DS3231_I2C_ADDRESS 0x68 //default address for DS3231
#define wakePin 3
unsigned int timeToSleep = 1; //use this to control the value going to the clock register. Set it to the # minutes desired to log
int timeCount = 0; //don't modify this

SPISettings mySettings;
DS3231 clock;

//temp sensor var's
int DS18S20_Pin = 2; //DS18S20 Signal pin on digital 2
OneWire ds(DS18S20_Pin); // on digital pin 2


// Convert normal decimal numbers to binary coded decimal
byte decToBcd(byte val)
{
  return ( (val / 10 * 16) + (val % 10) );
}
// Convert binary coded decimal to normal decimal numbers
byte bcdToDec(byte val)
{
  return ( (val / 16 * 10) + (val % 16) );
}

/*
 * Read the temperature and return it in DEG F
 */
float getTemp() {
  //returns the temperature from one DS18S20 in DEG Fahrenheit

  byte data[12];
  byte addr[8];

  if ( !ds.search(addr)) {
    //no more sensors on chain, reset search
    ds.reset_search();
    Serial.println("Cannot find an address for temp sensor");
    return -1000;
  }

  if ( OneWire::crc8( addr, 7) != addr[7]) {
    Serial.println("CRC is not valid!");
    return -1001;
  }

  if ( addr[0] != 0x10 && addr[0] != 0x28) {
    Serial.print("Device is not recognized");
    return -1002;
  }

  ds.reset();
  ds.select(addr);
  ds.write(0x44, 1); // start conversion, with parasite power on at the end

  byte present = ds.reset();
  ds.select(addr);
  ds.write(0xBE); // Read Scratchpad


  for (int i = 0; i < 9; i++) { // we need 9 bytes
    data[i] = ds.read();
  }

  ds.reset_search();

  byte MSB = data[1];
  byte LSB = data[0];

  float tempRead = ((MSB << 8) | LSB); //using two's compliment
  float TemperatureSum = tempRead / 16;
  TemperatureSum *= 1.8;
  TemperatureSum += 32;

  return TemperatureSum;

}

/*
 * Set up and initialize the SD card
 */
void sd_setup() {


#if DEBUG
    Serial.print("Initializing SD card...");
#endif

  const int chipSelect = 8;  //set chip select to whichever pin the sd reader's chip select goes to on the arduino

  // make sure that the default chip select pin is set to
  // output, even if you don't use it:
  pinMode(10, OUTPUT);

  // The chipSelect pin you use should also be set to output
  pinMode(chipSelect, OUTPUT);

  // see if the card is present and can be initialized:
  if (!SD.begin(chipSelect)) {
    Serial.println("Card failed, or not present");
    // don't do anything more:
    return;
  }

#if DEBUG
      Serial.println("card initialized.");
#endif

}


/*
 * Read the current time from the clock
 */
void readDS3231time(byte *second,
byte *minute,
byte *hour,
byte *dayOfWeek,
byte *dayOfMonth,
byte *month,
byte *year)
{
  Wire.beginTransmission(DS3231_I2C_ADDRESS);
  Wire.write(0); // set DS3231 register pointer to 00h
  Wire.endTransmission();
  Wire.requestFrom(DS3231_I2C_ADDRESS, 7);
  // request seven bytes of data from DS3231 starting from register 00h
  *second = bcdToDec(Wire.read() & 0x7f);
  *minute = bcdToDec(Wire.read());
  *hour = bcdToDec(Wire.read() & 0x3f);
  *dayOfWeek = bcdToDec(Wire.read());
  *dayOfMonth = bcdToDec(Wire.read());
  *month = bcdToDec(Wire.read());
  *year = bcdToDec(Wire.read());
}

/*
 * Print the current time to serial
 */
void printTime(void){

  byte second, minute, hour, dayOfWeek, dayOfMonth, month, year;
  // retrieve data from DS3231
  readDS3231time(&second, &minute, &hour, &dayOfWeek, &dayOfMonth, &month,
  &year);
 
    // print to the serial port too:
    Serial.print(month);
    Serial.print("/");
    Serial.print(dayOfMonth);
    Serial.print("/");
    Serial.print(year);
    Serial.print(" ");
    Serial.print(hour);
    Serial.print(":");
    Serial.print(minute);
    Serial.print(":");
    Serial.println(second);
    
}

/*
 *Write a string to sd 
 */
void write_Text_To_Disk(String filename,String str) {
  // open the file. note that only one file can be open at a time,
  // so you have to close this one before opening another.
  File dataFile = SD.open(filename, FILE_WRITE);  
  // if the file is available, write to it:
  if (dataFile) {
    delay(50);
    dataFile.print(str);
    dataFile.print("\n");
    dataFile.close();
  }
  else{
    
    Serial.println("error writing text to disk data.txt");
  }
}

/*
 * Write the temperature and time data to SD
 */
void append_to_disk(float temp) {

  // open the file. note that only one file can be open at a time,
  // so you have to close this one before opening another.
  File dataFile = SD.open("data.txt", FILE_WRITE);  
  
  byte second, minute, hour, dayOfWeek, dayOfMonth, month, year;
  // retrieve data from DS3231
  readDS3231time(&second, &minute, &hour, &dayOfWeek, &dayOfMonth, &month,
  &year);
 
  // if the file is available, write to it:
  if (dataFile) {
    delay(50);

//Currently off
#if 0
    //185 is what temp sensor returns on boot
    //don't write -1000 either
    if (temp == 185.0 || temp == -1000)
      return;
#endif 

#if DEBUG
     Serial.println("Printing Data:");
#endif

 //print comma seperated values in form:
    //mm/dd/yy hh:mm
    dataFile.print(temp);
    dataFile.print(", ");
    dataFile.print(month);
    dataFile.print("/");
    dataFile.print(dayOfMonth);
    dataFile.print("/");
    dataFile.print(year);
    dataFile.print(" ");
    dataFile.print(hour);
    dataFile.print(":");
    dataFile.print(minute);
    dataFile.print(":");
    dataFile.println(second);
    dataFile.close();
    
#if DEBUG
  printTime();
#endif    

  }
  // if the file isn't open, pop up an error:
  else {
    Serial.println("error opening data.txt");
  }
}

/*
 * on clock alarm, this function wakes up the device to
 * allow the loop to log data and go back to sleep
 * 
*/
void alarmISR()
{
  sleep_disable();
}

/*
 * Initialize the clock time, the alarm for the clock,
 * and the interrupt associated with it
 */
void setup_Clock(void) {

  // Initialize DS3231
  clock.begin();

  // Disarm alarms and clear alarms for this example, because alarms is battery backed.
  // Under normal conditions, the settings should be reset after power and restart microcontroller.
  clock.armAlarm1(false);
  clock.armAlarm2(false);
  clock.clearAlarm1();
  clock.clearAlarm2();

  // Set Alarm1 - Every 20s in each minute
  // setAlarm1(Date or Day, Hour, Minute, Second, Mode, Armed = true)
   clock.setAlarm1(0, 0, 0, 1, DS3231_MATCH_S);


  // Attach Interrput to wakePin. In Arduino Pro Mini connect DS3231 INT to Arduino Pin 3
  attachInterrupt(digitalPinToInterrupt(wakePin), alarmISR, FALLING);
  clock.enableOutput(false);
//  delay(1);

}

//read the device id and print it to the sd card
void eepromPrintToSD(){
  //if the id has already been saved, then go ahead and rewrite the file
  File dataFile = SD.open("device_id.txt", O_WRITE | O_CREAT | O_TRUNC);  
  if(dataFile){
    int addr = 0;
    Serial.println("writing device id");
    for (; addr < 4; addr++){
      if (addr == EEPROM.length()) {
         addr = 0;
       }
      Serial.print(EEPROM.read(addr));
      dataFile.print(EEPROM.read(addr));
    }
    
    dataFile.close();
  }
  
}

//set up the pin modes, the clock
//sd card, temp sensor, and the 
//device id
void setup(void) {

#if DEBUG
  Serial.begin(9600);
#endif 

  // set pin 4 (POWA) to output so that we can control the
  //power of the sd reader and the temp sensor
  pinMode(POWA, OUTPUT);

  // turn on SD card and temp sensor
  digitalWrite(POWA, HIGH);

  // give some delay to ensure RTC and SD are initialized properly
  delay(1);


  //set up sd card
  sd_setup();
  
  //set up clock
  setup_Clock();

  Wire.begin();

  #if DEBUG_TO_SD
    write_Text_To_Disk("data.txt","setting up system");
  #endif 

  //eepromSetup(); //COMMENT THIS OUT AFTER INITIAL PROGRAMMING
  eepromPrintToSD();
}


//continually log data every timeToSleep (specified at the top) number of minutes.
//go to sleep inbetween. The clock wakes the arduino up every minute to check if
//timeToSleep number of minutes have elapsed
void loop(void) {

#if DEBUG
    Serial.println("Going to sleep");
#endif

#if DEBUG_TO_SD
  write_Text_To_Disk("data.txt","Going To Sleep");
#endif 

  //enter deep sleep
  sleep_enable();
  set_sleep_mode(SLEEP_MODE_PWR_DOWN);
  cli();
  sleep_bod_disable();
  sei();
  
#if DEBUG
  //let serial have time print
  delay(75);
#endif
  sleep_cpu();


#if DEBUG
    printTime();
#endif
#if 0
    Serial.print("timeCount is ");
    Serial.println(timeCount);
    Serial.println("isAlarm() is: " + String(clock.isAlarm1(false)));
#endif

   //update until we hit the specified number of minutes to run
   
#if 0
  Serial.print("clock.isAlarm1() " + String(clock.isAlarm1(false)) + " && (timeCount < (timeToSleep - 1)) : ");  
  Serial.println(clock.isAlarm1(false) && (timeCount < (timeToSleep - 1)));
#endif

 //check if the desired number of minutes to sleep has elapsed
   if(clock.isAlarm1(false) && (timeCount < (timeToSleep - 1))){
      timeCount++;
      clock.clearAlarm1();
    }
    
  //At this point, the ISR from the clock alarm woke up the
  //processor and the alarm flag is set. Clear the flag and
  //handle whatever we need to do.
  else {
#if DEBUG_TO_SD
  write_Text_To_Disk("data.txt","Woke up, logging");
#endif    

#if DEBUG
    Serial.println("ALARM TRIGGERED!");
#endif        
delay(1);

    //we finally hit the specified number of minutes to run, reset and log
    timeCount = 0;
    
    float temperature = getTemp();
    delay(5);
    append_to_disk(temperature);
#if DEBUG
    Serial.println(temperature);
#endif        
    

    //clear alarm flag and update time To sleep
    clock.clearAlarm1();

  }


}

