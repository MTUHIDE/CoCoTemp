/*
  This code logs the temperature from a DS18B20 temperature sensor
  and prints the temperature along with a time stamp and unique UUID using a
  DS3231 Clock and a SparkFun microSD Transflash Breakout
  It also reads and logs the voltage

  This uses code from http://bildr.org/2011/07/ds18b20-arduino/
   and https://learn.sparkfun.com/tutorials/microsd-shield-and-sd-breakout-hookup-guide sd card code
   and http://tronixstuff.com/2014/12/01/tutorial-using-ds1307-and-ds3231-real-time-clock-modules-with-arduino/ using the clock
   and https://github.com/OSBSS/TRH/blob/master/TRH.ino main code body
   and https://github.com/jarzebski/Arduino-DS3231 library we use for clock interrupts
   and https://github.com/sirleech/TrueRandom for UUID
   and https://startingelectronics.org/articles/arduino/measuring-voltage-with-arduino/ for voltage sensor
   and was modified to save the temperature, in fahrenheit, and time data to a microSD card

  @author Humane Interface Design Enterprise, Michigan Tech
    -Nicholas Lanter
    -Kyle Bray
    -Stephen Radachy
    
    NOTES:
    1) BEFORE UPLOADING THIS PROGRAM, FIRST UPLOAD THE ARDUINOSETUP PROGRAM!!!

    2) On power up, the temperature sensor sends 185 deg F to verify booting

    3) Currently, the processor wakes up every minute to see if the specified
       timeToSleep minutes have passed, and then it logs when true. This is due
       to the nature of the RTC's alarm, and with some math it can be configured
       to wake up only once every timeToSleep minutes
*/

//Libraries
#include <OneWire.h> //temp sensor
#include <SPI.h>
#include "SdFat.h" //sd card
SdFat SD;
#include "Wire.h"
#include <avr/sleep.h>  //sleep library
#include <DS3231.h> //clock
#include "TrueRandom.h" //https://github.com/sirleech/TrueRandom

//debug settings
#define DEBUG 0 //0 debug off, 1 debug on. turn off when unnecessary to preserve power
#define DEBUG_TO_SD 0 //0 debug off, 1 debug on

//sd card variables
#define POWA 4    // pin 4 supplies power to microSD card breakout and temp sensor
const String filename = "data.txt"; //name of file we log data to
const int chipSelect = 8;  //set chip select to whichever pin the sd reader's chip select goes to on the arduino. default is 10

//clock variables
#define DS3231_I2C_ADDRESS 0x68 //default address for DS3231 clock
#define wakePin 3 //pin that comes from clock and wakes the arduino from sleep
const unsigned int timeToSleep = 1; //use this to control the value going to the clock register. Set it to the # minutes desired to log
unsigned int timeCount = 0; //don't modify this, used internally to keep track of when to wake from sleep
SPISettings mySettings;
DS3231 clock;

//temp sensor variables
const int DS18S20_Pin = 2; //DS18S20 Signal pin on digital 2
OneWire ds(DS18S20_Pin); // on digital pin 2
float temperature = -12345;

//voltage divider variables
#define NUM_VOLTAGE_SAMPLES 10
int sum = 0;                    // sum of samples taken
unsigned int sample_count = 0; // current sample number
float voltage = 0.0;            // calculated voltage

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
#if DEBUG
    Serial.println("Cannot find an address for temp sensor");
#endif
    return -1000;
  }

  if ( OneWire::crc8( addr, 7) != addr[7]) {
#if DEBUG    
    Serial.println("CRC is not valid!");
#endif
    return -1001;
  }

  if ( addr[0] != 0x10 && addr[0] != 0x28) {
#if DEBUG    
    Serial.print("Device is not recognized");
#endif
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

  // The chipSelect pin you use should also be set to output
  pinMode(chipSelect, OUTPUT);

  // see if the card is present and can be initialized:
  if (!SD.begin(chipSelect)) {
#if DEBUG
    Serial.println("Card failed, or not present");
#endif
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
#if DEBUG
  // print to the serial port:
  //yyyy-mm-dd hh:mm:ss
  Serial.print(year);
  Serial.print("-");
  Serial.print(month);
  Serial.print("-");
  Serial.print(dayOfMonth);
  Serial.print(" ");
  Serial.print(hour);
  Serial.print(":");
  Serial.print(minute);
  Serial.print(":");
  Serial.print(second);
#endif
}

/*
 *Write a string Str to sd card 
 *file's name = filename
 */
void write_Text_To_Disk(String str) {
  // open the file. note that only one file can be open at a time,
  // so you have to close this one before opening another.
  File dataFile = SD.open(filename, FILE_WRITE);
  // if the file is available, write to it:
  if (dataFile) {
    delay(50);
    dataFile.print(str);
    dataFile.close();
  }
  else{
#if DEBUG
    Serial.println("error writing text to disk data.txt");
#endif
  }
}

/*
 * Write the temperature and time data to SD
 * Pads with 0s if necessary.
 * Does not print a new line since that comes after the voltage reading
 */
void append_Temp_To_Disk(float temp) {

  // open the file. note that only one file can be open at a time,
  // so you have to close this one before opening another.
  File dataFile = SD.open(filename, FILE_WRITE);

  // if the file is available, write to it:
  if (dataFile) {
    delay(50);

  byte second, minute, hour, dayOfWeek, dayOfMonth, month, year;
  // retrieve data from DS3231
  readDS3231time(&second, &minute, &hour, &dayOfWeek, &dayOfMonth, &month,
  &year);

#if DEBUG
  Serial.println("Printing Data:");
#endif

  //print comma seperated values in form:
  //yyyy-mm-dd hh:mm:ss,temperature
  //Pad with 20 to make year XXXX
  dataFile.print("20");
  dataFile.print(year);
  dataFile.print("-");
  
  //Pad with zero
    if(month<10){
      dataFile.print("0");
    }
  dataFile.print(month);
  dataFile.print("-");
  
  //Pad with zero
    if(dayOfMonth<10){
      dataFile.print("0");
    }
  dataFile.print(dayOfMonth);
  dataFile.print(" ");
  
  //Pad with zero
    if(hour<10){
      dataFile.print("0");
    }
  dataFile.print(hour);
  dataFile.print(":");
  
  //Pad with zero
    if(minute<10){
      dataFile.print("0");
    }
  dataFile.print(minute);
  dataFile.print(":");
  
  //Pad with zero
    if(second<10){
      dataFile.print("0");
    }
  dataFile.print(second);
  dataFile.print(",");
  //dataFile.println(temp);
  dataFile.close();

#if DEBUG
  printTime();
#endif

  }
  // if the file isn't open, pop up an error:
  else {
#if DEBUG
    Serial.println("error opening data.txt");
#endif
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

/*Generate and print UUID to disk
 * https://github.com/sirleech/TrueRandom
 */
void print_UUID_To_Disk() {
  File dataFile = SD.open(filename, FILE_WRITE);

  // if the file is available, write to it:
  if (dataFile) {
    delay(50);
    byte uuidNumber[16];
    TrueRandom.uuid(uuidNumber);
    
    for (int i=0; i<16; i++) {
      if (i==4) dataFile.print("-");
      if (i==6) dataFile.print("-");
      if (i==8) dataFile.print("-");
      if (i==10) dataFile.print("-");
      
      int topDigit = uuidNumber[i] >> 4;
      int bottomDigit = uuidNumber[i] & 0x0f;
      // Print high hex digit
#if DEBUG
      Serial.print( "0123456789ABCDEF"[topDigit]);
      Serial.print("0123456789ABCDEF"[bottomDigit]);
#endif
      dataFile.print( "0123456789ABCDEF"[topDigit] );
      // Low hex digit
      dataFile.print( "0123456789ABCDEF"[bottomDigit] );
    }
#if DEBUG
    Serial.println();
#endif

    dataFile.close();
  }
}

/*
 * Initialize the clock time, the alarm for the clock,
 * and the interrupt associated with it which is used
 * to wake the arduino from sleep
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


/*set up the pin modes, the clock
/sd card, temp sensor, and the device id
*/
void setup(void) {

#if DEBUG
  Serial.begin(9600);
#endif

  //set up sd card
  sd_setup();

  //set up clock
  setup_Clock();

  //Temp sensor
  Wire.begin();

  #if DEBUG_TO_SD
    write_Text_To_Disk("setting up system");
  #endif

}


//continually log data every timeToSleep (specified at the top) number of minutes.
//go to sleep inbetween. The clock wakes the arduino up every minute to check if
//timeToSleep number of minutes have elapsed
void loop(void) {

#if DEBUG
    Serial.println("Going to sleep");
#endif

#if DEBUG_TO_SD
  write_Text_To_Disk("Going To Sleep");
#endif

  //enter deep sleep
  sleep_enable();
  set_sleep_mode(SLEEP_MODE_PWR_DOWN);
  cli();
  sleep_bod_disable();
  sei();

#if DEBUG
  //let serial have time print before sleep
  delay(75);
#endif

  sleep_cpu();


#if DEBUG
    printTime();
    Serial.print("timeCount is ");
    Serial.println(timeCount);
    Serial.println("isAlarm() is: " + String(clock.isAlarm1(false)));
#endif

   //update until we hit the specified number of minutes to run

#if DEBUG
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
#if DEBUG
    Serial.println("ALARM TRIGGERED!");
#endif
    
    // calculate the voltage
    //https://startingelectronics.org/articles/arduino/measuring-voltage-with-arduino/
    // take a number of analog samples and add them up
    while (sample_count < NUM_VOLTAGE_SAMPLES) {
        sum += analogRead(A3);
        /*REMOVE*/
#if DEBUG_TO_SD
        write_Text_To_Disk("sum = "+String(sum)+"\n");
#endif
#if DEBUG       
        Serial.println("sum = "+String(sum)+"\n");
#endif
        sample_count++;
        delay(10);
    }
    // use 5.0 for a 5.0V ADC reference voltage
    // 5.015V is the calibrated reference voltage
    voltage = ((float)sum / (float)NUM_VOLTAGE_SAMPLES * 5) / 1024.0;
    sample_count = 0;
    sum = 0;
    voltage *= 11;
    // send voltage for display on Serial Monitor
    // voltage multiplied by 11 when using voltage divider that
    // divides by 11. 11.132 is the calibrated voltage divide
    // value
#if DEBUG
    Serial.print(voltage);
    Serial.println (" V");
#endif    
 
    //we finally hit the specified number of minutes to run, log & reset 
    timeCount = 0;
    temperature = getTemp();
    delay(5);

    //log uuid, temp, voltage
    print_UUID_To_Disk();
    write_Text_To_Disk(", ");
    append_Temp_To_Disk(temperature);
    write_Text_To_Disk(" "+String(voltage)+"\n");
    voltage = -5000;
    
#if DEBUG
    Serial.println(temperature);
#endif

    temperature = -54321; //set to this so we dont get the same temp reading each time 
    //if something is wrong with sensor
    
    //clear alarm flag and update time To sleep
    clock.clearAlarm1();

  }

}

