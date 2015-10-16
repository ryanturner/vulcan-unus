#ifndef __EEPROMH
#define __EEPROMH

#define EEPROM_OFFSET 100

#define EEPROM_VERSION "S01"

#define axis_steps_per_unit_address (EEPROM_OFFSET + 4*sizeof(char))
#define max_feedrate_address (axis_steps_per_unit_address + 4*sizeof(float))
#define max_acceleration_units_per_sq_second_address (max_feedrate_address + 4*sizeof(float))
#define move_acceleration_address (max_acceleration_units_per_sq_second_address + 4*sizeof(long))
#define retract_acceleration_address (move_acceleration_address + sizeof(float))
#define mintravelfeedrate_address (retract_acceleration_address + sizeof(float))
#define minimumfeedrate_address (mintravelfeedrate_address + sizeof(float))
#define max_xy_jerk_address (minimumfeedrate_address + sizeof(float))
#define max_z_jerk_address (max_xy_jerk_address + sizeof(float))
#define max_e_jerk_address (max_z_jerk_address + sizeof(float))
#define min_seg_time_address (max_e_jerk_address + sizeof(float))
#define Kp_address (min_seg_time_address + sizeof(unsigned long))
#define Ki_address (Kp_address + sizeof(unsigned int))
#define Kd_address (Ki_address + sizeof(unsigned int))

extern void EEPROM_RetrieveSettings(bool def, bool printout );
extern void EEPROM_printSettings();
extern void EEPROM_StoreSettings();


#endif
