package co.vulcanus.dux.ui.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;

import co.vulcanus.dux.R;
import co.vulcanus.dux.model.DeviceState;
import co.vulcanus.dux.model.Pin;
import co.vulcanus.dux.service.ApiService;
import co.vulcanus.dux.util.Constants;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {
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
        button1 = (Button) view.findViewById(R.id.button);
        button1.setOnClickListener(this);
        button2 = (Button) view.findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button3 = (Button) view.findViewById(R.id.button3);
        button3.setOnClickListener(this);
        button4 = (Button) view.findViewById(R.id.button4);
        button4.setOnClickListener(this);
        button5 = (Button) view.findViewById(R.id.button5);
        button5.setOnClickListener(this);
        button6 = (Button) view.findViewById(R.id.button6);
        button6.setOnClickListener(this);
        button7 = (Button) view.findViewById(R.id.button7);
        button7.setOnClickListener(this);
        button8 = (Button) view.findViewById(R.id.button8);
        button8.setOnClickListener(this);
        this.deviceState = new DeviceState();
        return view;
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

    private void togglePin(View v, int pin) {
        int pinValue = this.deviceState.getPinValue(pin);
        if(pinValue == 1) {
            this.deviceState.setPin(pin, 0);
            v.setBackgroundColor(Color.parseColor("#ffff4444"));
        } else {
            this.deviceState.setPin(pin, 1);
            v.setBackgroundColor(Color.parseColor("#ffffffff"));
        }
    }
}
