#include <Bridge.h>
#include <YunClient.h>
#include <FileIO.h>
#include <HttpClient.h>
#include <Process.h>
#include <YunServer.h>
#include <Mailbox.h>
#include <Console.h>
#include <ArduinoJson.h>
int switchPins[] = {2,3,4,5,6,7,8,9};
int switchPinCount = 8;
void setup() {
  Serial.begin(9600);
  Bridge.begin();
  Mailbox.begin();
  for(int i = 0; i < switchPinCount; i++){
    pinMode(switchPins[i], OUTPUT);
    digitalWrite(switchPins[i], HIGH);
  }
}

void loop() {
  Serial.println("Looping");
  String message;
  while (Mailbox.messageAvailable())
  {
    Serial.println("Found a message!");
    Mailbox.readMessage(message);
    Serial.println(message);
    if(message.length() == 49) {
      StaticJsonBuffer<200> jsonBuffer;
      JsonObject& root = jsonBuffer.parseObject(message);
      if (!root.success())
      {
        Serial.println("parseObject() failed");
        return;
      }
      for(int i = 0; i < switchPinCount; i++){
        String pin = String(switchPins[i]);
        digitalWrite(switchPins[i], root[pin]);
        Serial.println("Setting pin " + pin + " to " + switchPins[i]);
      }
    }
  }
}
