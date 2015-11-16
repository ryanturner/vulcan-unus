package co.vulcanus.dux.ui.activity;

import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.ViewGroup;

import com.macroyau.blue2serial.BluetoothDeviceListDialog;
import com.macroyau.blue2serial.BluetoothSerial;
import com.macroyau.blue2serial.BluetoothSerialListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import co.vulcanus.dux.R;
import co.vulcanus.dux.util.Constants;

/**
 * Created by ryan_turner on 10/20/15.
 */
public class DuxPreferenceActivity extends PreferenceActivity {
    private LinkedHashMap<String, String> bluetoothSerial;

    public LinkedHashMap<String, String> getBluetoothSerial() {
        return bluetoothSerial;
    }

    public void setBluetoothSerial(LinkedHashMap<String, String> bluetoothSerial) {
        this.bluetoothSerial = bluetoothSerial;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {

            } else {
                HashMap <String, String> bluetoothSerial = (HashMap <String, String>) extras.get(Constants.BLUETOOTH_SERIAL);
                LinkedHashMap <String, String> bluetoothSerialA = new LinkedHashMap<String, String>(bluetoothSerial);
                setBluetoothSerial(bluetoothSerialA);
            }
        }
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            MultiSelectListPreference bluetoothDevice = (MultiSelectListPreference) findPreference(getString(R.string.bluetooth_device));
            DuxPreferenceActivity duxPreferenceActivity = (DuxPreferenceActivity) getActivity();
            if(duxPreferenceActivity != null && duxPreferenceActivity.getBluetoothSerial() != null) {
                bluetoothDevice.setEntries((String[]) duxPreferenceActivity.getBluetoothSerial().values().toArray());
            } else {
                String[] emptyEntries = new String[1];
                emptyEntries[0] = "Add a bluetooth device";
                bluetoothDevice.setEntries(emptyEntries);
                bluetoothDevice.setEntryValues(emptyEntries);
            }
        }

    }
}