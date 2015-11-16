package co.vulcanus.dux.ui.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.macroyau.blue2serial.BluetoothDeviceListDialog;
import com.macroyau.blue2serial.BluetoothSerial;
import com.macroyau.blue2serial.BluetoothSerialListener;

import java.util.HashMap;
import java.util.LinkedHashMap;

import co.vulcanus.dux.R;
import co.vulcanus.dux.ui.fragment.MainActivityFragment;
import co.vulcanus.dux.util.Constants;

public class MainActivity extends AppCompatActivity implements BluetoothSerialListener {
    private MainActivityFragment mainActivityFragment;
    private boolean crlf = false;
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private MenuItem actionConnect, actionDisconnect, actionAdd;
    public BluetoothSerial bluetoothSerial;
    SharedPreferences SP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(savedInstanceState == null) {
            mainActivityFragment = new MainActivityFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.linear_layout_parent, mainActivityFragment)
                    .commit();
        }
        SP = PreferenceManager.getDefaultSharedPreferences(this);
        bluetoothSerial = new BluetoothSerial(this, this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check Bluetooth availability on the device and set up the Bluetooth adapter
        bluetoothSerial.setup();
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Open a Bluetooth serial port and get ready to establish a connection
        if (bluetoothSerial.isBluetoothEnabled()) {
            if (!bluetoothSerial.isConnected()) {
                bluetoothSerial.start();
                connectBluetooth();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Disconnect from the remote device and close the serial port
        bluetoothSerial.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        actionConnect = menu.findItem(R.id.action_connect);
        actionDisconnect = menu.findItem(R.id.action_disconnect);
        actionAdd = menu.findItem(R.id.action_add);
        actionAdd.setVisible(false);
        invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, DuxPreferenceActivity.class);
            LinkedHashMap<String, String> devices = new LinkedHashMap<String, String>();
            for(BluetoothDevice bt : bluetoothSerial.getPairedDevices()) {
                devices.put(bt.getAddress(), bt.getName());
            }
            i.putExtra(Constants.BLUETOOTH_SERIAL, devices);
            startActivity(i);
            return true;
        } else if (id == R.id.action_edit_layout) {
            if (item.getTitle().toString().equals(getString(R.string.edit_layout))) {
                item.setTitle(R.string.set_layout);
                actionAdd.setVisible(true);
            } else {
                item.setTitle(R.string.edit_layout);
                actionAdd.setVisible(false);
            }
            mainActivityFragment.editLayoutPressed();
            return true;
        } else if (id == R.id.action_connect) {
            connectBluetooth();
            return true;
        } else if (id == R.id.action_disconnect) {
            bluetoothSerial.stop();
            return true;
        } else if (id == R.id.action_add) {
            mainActivityFragment.onClick(mainActivityFragment.addButton());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        getSupportFragmentManager().putFragment(savedInstanceState, "mainActivityFragment", mainActivityFragment);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mainActivityFragment = (MainActivityFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mainActivityFragment");

    }

    @Override
    public void invalidateOptionsMenu() {
        if (bluetoothSerial == null)
            return;

        // Show or hide the "Connect" and "Disconnect" buttons on the app bar
        if (bluetoothSerial.isConnected()) {
            if (actionConnect != null)
                actionConnect.setVisible(false);
            if (actionDisconnect != null)
                actionDisconnect.setVisible(true);
        } else {
            if (actionConnect != null)
                actionConnect.setVisible(true);
            if (actionDisconnect != null)
                actionDisconnect.setVisible(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ENABLE_BLUETOOTH:
                // Set up Bluetooth serial port when Bluetooth adapter is turned on
                if (resultCode == Activity.RESULT_OK) {
                    bluetoothSerial.setup();
                }
                break;
        }
    }

    private void updateBluetoothState() {
        // Get the current Bluetooth state
        final int state;
        if (bluetoothSerial != null)
            state = bluetoothSerial.getState();
        else
            state = BluetoothSerial.STATE_DISCONNECTED;

        // Display the current state on the app bar as the subtitle
        String subtitle;
        switch (state) {
            case BluetoothSerial.STATE_CONNECTING:
                subtitle = getString(R.string.status_connecting);
                break;
            case BluetoothSerial.STATE_CONNECTED:
                subtitle = getString(R.string.status_connected, bluetoothSerial.getConnectedDeviceName());
                break;
            default:
                subtitle = getString(R.string.status_disconnected);
                break;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(subtitle);
        }
    }
    /* Implementation of BluetoothSerialListener */

    @Override
    public void onBluetoothNotSupported() {
        new MaterialDialog.Builder(this)
                .content(R.string.no_bluetooth)
                .positiveText(R.string.action_quit)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        finish();
                    }
                })
                .cancelable(false)
                .theme(Theme.DARK)
                .show();
    }

    @Override
    public void onBluetoothDisabled() {
        Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBluetooth, REQUEST_ENABLE_BLUETOOTH);
    }

    @Override
    public void onBluetoothDeviceDisconnected() {
        invalidateOptionsMenu();
        updateBluetoothState();
    }

    @Override
    public void onConnectingBluetoothDevice() {
        updateBluetoothState();
    }

    @Override
    public void onBluetoothDeviceConnected(String name, String address) {
        invalidateOptionsMenu();
        updateBluetoothState();
    }

    @Override
    public void onBluetoothSerialRead(String message) {
        // Print the incoming message on the terminal screen
//        tvTerminal.append(getString(R.string.terminal_message_template,
//                bluetoothSerial.getConnectedDeviceName(),
//                message));
//        svTerminal.post(scrollTerminalToBottom);
        Log.e(Constants.LOG_TAG, message);
    }

    @Override
    public void onBluetoothSerialWrite(String message) {
        // Print the outgoing message on the terminal screen
        Log.e(Constants.LOG_TAG, message);
        //tvTerminal.append(getString(R.string.terminal_message_template,
        //        bluetoothSerial.getLocalAdapterName(),
        //        message));
        //svTerminal.post(scrollTerminalToBottom);
    }

    private void connectBluetooth() {
        String bluetoothHost = SP.getString(getString(R.string.bluetooth_device), "");
        if(!bluetoothHost.isEmpty()) {
            BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(bluetoothHost);
            if(device != null) {
                bluetoothSerial.connect(device);
            }
        }
    }
}
