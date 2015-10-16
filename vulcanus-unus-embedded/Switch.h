#ifndef Switch_h
#define Switch_h

#include "Arduino.h"
class Switch {
  public:
    Switch(int id, int pin, char label[], int groupId, boolean isGroupMaster, int defaultDuration);
    int getId();
    void setId(int id);
    int getPin();
    void setPin(int pin);
    char[] getLabel();
    void setLabel(char label[]);
    boolean getIsGroupMaster();
    void setIsGroupMaster(boolean isGroupMaster);
    int getDefaultDuration();
    void setDefaultDuration(int defaultDuration);
  private:
    int _id;
    int _pin;
    char _label[];
    int _groupId;
    boolean _isGroupMaster;
    int _defaultDuration;
};  
#endif
