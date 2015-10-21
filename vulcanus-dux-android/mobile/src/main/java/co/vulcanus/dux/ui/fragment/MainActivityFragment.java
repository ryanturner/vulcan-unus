package co.vulcanus.dux.ui.fragment;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import co.vulcanus.dux.R;
import co.vulcanus.dux.model.DeviceState;
import co.vulcanus.dux.model.Pin;
import co.vulcanus.dux.service.RestClient;
import co.vulcanus.dux.util.Constants;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Button button7;
    Button button8;
    DeviceState deviceState;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.linearLayout);


        for(int i = 0; i < linearLayout.getChildCount(); i++) {
            Button button = (Button) linearLayout.getChildAt(i);
            button.setOnClickListener(this);

        }
        this.setDeviceState();
        return view;
    }
    public void setDeviceState() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String firstPin = SP.getString(Constants.EMBEDDED_FIRST_PIN,
                Constants.EMBEDDED_FIRST_PIN_DEFAULT);
        String numberOfRelays = SP.getString(Constants.EMBEDDED_NUMBER_OF_RELAYS,
                Constants.EMBEDDED_NUMBER_OF_RELAYS_DEFAULT);
        this.deviceState = new DeviceState(Integer.parseInt(firstPin), Integer.parseInt(firstPin) +
                Integer.parseInt(numberOfRelays));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button: {
                this.togglePin(v, 2);
                break;
            }
            case R.id.button2: {
                this.togglePin(v, 3);
                break;
            }
            case R.id.button3: {
                this.togglePin(v, 4);
                break;
            }
            case R.id.button4: {
                this.togglePin(v, 5);
                break;
            }
            case R.id.button5: {
                this.togglePin(v, 6);
                break;
            }
            case R.id.button6: {
                this.togglePin(v, 7);
                break;
            }
            case R.id.button7: {
                this.togglePin(v, 8);
                break;
            }
            case R.id.button8: {
                this.togglePin(v, 9);
                break;
            }
            default: {

                break;
            }
        }
    }

    private void togglePin(View v, int pinNumber) {
        Pin pin = this.deviceState.getPin(pinNumber);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        if(SP.getBoolean(Constants.EMBEDDED_REVERSE_LOGIC, Constants.EMBEDDED_REVERSE_LOGIC_DEFAULT)) {
            v.setSelected(!pin.isHigh());
        } else {
            v.setSelected(pin.isHigh());
        }
        pin.setIsHigh(!pin.isHigh());
        this.deviceState.setPin(pin.getNumber(), pin);
        Call<String> call = RestClient.getInstance().getService().sendMailbox(this.deviceState);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        this.setDeviceState();
    }
}
