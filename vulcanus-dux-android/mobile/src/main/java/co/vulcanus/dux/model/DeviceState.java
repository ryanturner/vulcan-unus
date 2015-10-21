package co.vulcanus.dux.model;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import co.vulcanus.dux.model.serializer.DeviceStateSerializer;
import co.vulcanus.dux.service.ApiService;
import co.vulcanus.dux.util.Constants;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ryan_turner on 10/19/15.
 */
public class DeviceState {

    private Map<Integer, Pin> pins;

    private int firstPin;
    private int lastPin;
    public void setPin(int pin, Pin pinValue) {
        pins.put(pin, pinValue);
    }

    public Pin getPin(int pin) {
        return pins.get(pin);
    }

    public HashMap<Integer, Pin> createPins(int firstPin, int lastPin) {
        HashMap<Integer, Pin> pins = new HashMap<>();
        for(int i = firstPin; i < lastPin; i++) {
            Pin pin = new Pin();
            pin.setNumber(i);
            pin.setIsHigh(true);
            pins.put(i, pin);
        }
        return pins;
    }

    public DeviceState(int firstPin, int lastPin) {
        this.firstPin = firstPin;
        this.lastPin = lastPin;
        this.pins = this.createPins(firstPin, lastPin);
    }
    public int getFirstPin() {
        return this.firstPin;
    }
    public int getLastPin() {
        return this.lastPin;
    }
    public String toString() {
        com.google.gson.Gson gson = new GsonBuilder().registerTypeAdapter(DeviceState.class, new
                DeviceStateSerializer()).create();
        return gson.toJson(this);
    }
}
