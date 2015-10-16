#include "Arduino.h"
#include <string.h>
#include "Switch.h"

Switch::Switch(int id, int pin, String label, int groupId, boolean isGroupMaster, int defaultDuration) {
}
int Switch::getId() {
  return _id;
}
void Switch::setId(int id) {
}
int Switch::getPin() {
  return _pin;
}
void Switch::setPin(int pin) {
  _pin = pin;
}
String Switch::getLabel() {
  return _label;
}
void Switch::setLabel(String label) {
  _label = label;
}
boolean Switch::getIsGroupMaster() {
  return _isGroupMaster;
}
void Switch::setIsGroupMaster(boolean isGroupMaster) {
  _isGroupMaster = isGroupMaster;
}
int Switch::getDefaultDuration() {
  return _defaultDuration;
}
void Switch::setDefaultDuration(int defaultDuration) {
  _defaultDuration = defaultDuration;
}
