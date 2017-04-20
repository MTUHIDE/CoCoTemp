//READ ME!!!!
//NOTE: THIS SENDS THE TEMP TO THE PHONE ONCE THE PHONE SENDS A MESSAGE TO THE ARDUINO.
//SLOPPY CODE FOR PROOF OF CONCEPT. THIS DOES NOT LOG ANY DATA, IT ONLY HAS THE LIBRARIES AND FUNCTIONS
//FROM THE TEMP LOGGER AS WELL AS LIBRARIES AND FUNCTIONS FROM BLUETOOTH AND 
//IS ADAPTED TO ONLY SEND TEMP ONCE REQUESTED BY THE PHONE
//Make sure ble device is in cmd mode




//Libraries
#include <OneWire.h> //temp sensor
#include <SPI.h>
#include "SdFat.h" //sd card
SdFat SD;
#include "Wire.h"
#include <avr/sleep.h>  //sleep library
#include <DS3231.h> //clock

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

#include <Arduino.h>
#include <SPI.h>
#if not defined (_VARIANT_ARDUINO_DUE_X_) && not defined (_VARIANT_ARDUINO_ZERO_)
  #include <SoftwareSerial.h>
#endif

#include "Adafruit_BLE.h"
#include "Adafruit_BluefruitLE_SPI.h"
#include "Adafruit_BluefruitLE_UART.h"

#include "BluefruitConfig.h"

/*=========================================================================
    APPLICATION SETTINGS

    FACTORYRESET_ENABLE       Perform a factory reset when running this sketch
   
                              Enabling this will put your Bluefruit LE module
                              in a 'known good' state and clear any config
                              data set in previous sketches or projects, so
                              running this at least once is a good idea.
   
                              When deploying your project, however, you will
                              want to disable factory reset by setting this
                              value to 0.  If you are making changes to your
                              Bluefruit LE device via AT commands, and those
                              changes aren't persisting across resets, this
                              is the reason why.  Factory reset will erase
                              the non-volatile memory where config data is
                              stored, setting it back to factory default
                              values.
       
                              Some sketches that require you to bond to a
                              central device (HID mouse, keyboard, etc.)
                              won't work at all with this feature enabled
                              since the factory reset will clear all of the
                              bonding data stored on the chip, meaning the
                              central device won't be able to reconnect.
    MINIMUM_FIRMWARE_VERSION  Minimum firmware version to have some new features
    MODE_LED_BEHAVIOUR        LED activity, valid options are
                              "DISABLE" or "MODE" or "BLEUART" or
                              "HWUART"  or "SPI"  or "MANUAL"
    -----------------------------------------------------------------------*/
    #define FACTORYRESET_ENABLE         1
    #define MINIMUM_FIRMWARE_VERSION    "0.6.6"
    #define MODE_LED_BEHAVIOUR          "MODE"
/*=========================================================================*/

// Create the bluefruit object, either software serial...uncomment these lines

SoftwareSerial bluefruitSS = SoftwareSerial(BLUEFRUIT_SWUART_TXD_PIN, BLUEFRUIT_SWUART_RXD_PIN);

Adafruit_BluefruitLE_UART ble(bluefruitSS, BLUEFRUIT_UART_MODE_PIN,
                      BLUEFRUIT_UART_CTS_PIN, BLUEFRUIT_UART_RTS_PIN);


/* ...or hardware serial, which does not need the RTS/CTS pins. Uncomment this line */
 //Adafruit_BluefruitLE_UART ble(Serial, BLUEFRUIT_UART_MODE_PIN);

/* ...hardware SPI, using SCK/MOSI/MISO hardware SPI pins and then user selected CS/IRQ/RST */
//Adafruit_BluefruitLE_SPI ble(BLUEFRUIT_SPI_CS, BLUEFRUIT_SPI_IRQ, BLUEFRUIT_SPI_RST);

/* ...software SPI, using SCK/MOSI/MISO user-defined SPI pins and then user selected CS/IRQ/RST */
//Adafruit_BluefruitLE_SPI ble(BLUEFRUIT_SPI_SCK, BLUEFRUIT_SPI_MISO,
//                             BLUEFRUIT_SPI_MOSI, BLUEFRUIT_SPI_CS,
//                             BLUEFRUIT_SPI_IRQ, BLUEFRUIT_SPI_RST);


// A small helper
void error(const __FlashStringHelper*err) {
  Serial.println(err);
  while (1);
}

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
  
  //byte present = ds.reset();
  ds.reset();
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
  dataFile.print(temp);
  dataFile.print("\n");
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

  while (!Serial);  // required for Flora & Micro
  delay(500);

  Serial.begin(115200);
  Serial.println(F("Adafruit Bluefruit Command Mode Example"));
  Serial.println("---------------------------------------");

  /* Initialise the module */
  Serial.print(F("Initialising the Bluefruit LE module: "));

  if ( !ble.begin(VERBOSE_MODE) )
  {
    error(F("Couldn't find Bluefruit, make sure it's in CoMmanD mode & check wiring?"));
  }
  Serial.println( F("OK!") );

  if ( FACTORYRESET_ENABLE )
  {
    /* Perform a factory reset to make sure everything is in a known state */
    Serial.println(F("Performing a factory reset: "));
    if ( ! ble.factoryReset() ){
      error(F("Couldn't factory reset"));
    }
  }

  /* Disable command echo from Bluefruit */
  ble.echo(false);

  Serial.println("Requesting Bluefruit info:");
  /* Print Bluefruit information */
  ble.info();

  Serial.println(F("Please use Adafruit Bluefruit LE app to connect in UART mode"));
  Serial.println(F("Then Enter characters to send to Bluefruit"));
  Serial.println();

  ble.verbose(false);  // debug info is a little annoying after this point!

  /* Wait for connection */
  while (! ble.isConnected()) {
      delay(500);
  }

  // LED Activity cotlkdjsfmmand is only supported from 0.6.6
  if ( ble.isVersionAtLeast(MINIMUM_FIRMWARE_VERSION) )
  {
    // Change Mode LED Activity
    Serial.println(F("******************************"));
    Serial.println(F("Change LED activity to " MODE_LED_BEHAVIOUR));
    ble.sendCommandCheckOK("AT+HWModeLED=" MODE_LED_BEHAVIOUR);
    Serial.println(F("******************************"));
  }
}

int freeRam () 
{
  extern int __heap_start, *__brkval; 
  int v; 
  return (int) &v - (__brkval == 0 ? (int) &__heap_start : (int) __brkval); 
}

//continually log data every timeToSleep (specified at the top) number of minutes.
//go to sleep inbetween. The clock wakes the arduino up every minute to check if
//timeToSleep number of minutes have elapsed
void loop(void) {

  delay(200);
  // Check for user input
  char inputs[BUFSIZE+1];

  /*
  if ( getUserInput(inputs, BUFSIZE) )
  {
    delay(500);
    // Send characters to Bluefruit
    Serial.print("[Send] ");
    Serial.println(inputs);

    ble.print("AT+BLEUARTTX=");
    ble.println(inputs);

    // check response stastus
    if (! ble.waitForOK() ) {
      Serial.println(F("Failed to send?"));
    }
  }
*/
  // Check for incoming characters from Bluefruit
  ble.println("AT+BLEUARTRX");
  ble.readline();
  if (strcmp(ble.buffer, "OK") == 0) {
    // no data
    return;
  }
  // Some data was found, its in the buffer
  Serial.print(F("[Recv] ")); Serial.println(ble.buffer);
  ble.waitForOK();
  // Send characters to Bluefruit
    Serial.print("[Send] ");
    Serial.println(getTemp());

    ble.print("AT+BLEUARTTX=");
    ble.println(getTemp());
  
}

/**************************************************************************/
/*!
    @brief  Checks for user input (via the Serial Monitor)
*/
/**************************************************************************/
bool getUserInput(char buffer[], uint8_t maxSize)
{
  // timeout in 100 milliseconds
  TimeoutTimer timeout(100);

  memset(buffer, 0, maxSize);
  while( (!Serial.available()) && !timeout.expired() ) { delay(1); }

  if ( timeout.expired() ) return false;

  delay(2);
  uint8_t count=0;
  do
  {
    Serial.print("getting user input, buffer = ");
    Serial.println(buffer);
    count += Serial.readBytes(buffer+count, maxSize);
    delay(2);
  } while( (count < maxSize) && (Serial.available()) );

  return true;
}
