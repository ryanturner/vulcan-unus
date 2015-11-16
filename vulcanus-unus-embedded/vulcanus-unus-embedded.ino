#include <ArduinoJson.h>
#include <SoftwareSerial.h>

SoftwareSerial BT(10, 11); 
int switchPins[] = {2,3,4,5,6,7,8,9};
int switchPinCount = 8;
String message;

void setup() {
  Serial.begin(9600);
  BT.begin(9600);
  for(int i = 0; i < switchPinCount; i++){
    pinMode(i, OUTPUT);
    digitalWrite(i, HIGH);
  }
}

void loop() {
  while(BT.available())
  {//while there is data available on the serial monitor
    char mychar = BT.read();
    message += mychar;//store string from serial command
  }
  if(!BT.available()) {
    if(message!="") {//if data is available
      if(message.length() == 49) {
        StaticJsonBuffer<200> jsonBuffer;
        JsonObject& root = jsonBuffer.parseObject(message);
        if (!root.success())
        {
          Serial.println('{"status":"failure"}');
          return;
        }
        for(int i = 0; i < switchPinCount; i++){
          String pin = String(switchPins[i]);
          digitalWrite(switchPins[i], root[pin]);
        }
        root.printTo(Serial);
      }
      message=""; //clear the data
    }
  }
  delay(500);
}
