#ifndef __EEPROMH
#define __EEPROMH

#define EEPROM_OFFSET 100

#define EEPROM_VERSION "V00"

#define device_guid_address (EEPROM_OFFSET + 16)

#define max_feedrate_address (axis_steps_per_unit_address + 4*sizeof(float))
#define move_acceleration_address (max_acceleration_units_per_sq_second_address + 4*sizeof(long))
#define Kp_address (min_seg_time_address + sizeof(unsigned long))
#define Ki_address (Kp_address + sizeof(unsigned int))

extern void EEPROM_RetrieveSettings(bool def, bool printout );
extern void EEPROM_printSettings();
extern void EEPROM_StoreSettings();


#endif
