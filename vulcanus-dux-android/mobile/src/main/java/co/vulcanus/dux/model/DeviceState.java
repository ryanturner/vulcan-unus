package co.vulcanus.dux.model;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private Map<Integer, Integer> pins;

    private ApiService service;

    public void setPin(int pin, int pinValue) {
        pins.put(pin, pinValue);
        this.updateService();
    }

    public int getPinValue(int pin) {
        return pins.get(pin);
    }

    public DeviceState() {

        HashMap<Integer, Integer> pins = new HashMap<>();
        for(int i = 2; i < 10; i++) {
            pins.put(i, 1);
        }
        this.pins = pins;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.service = retrofit.create(ApiService.class);
    }
    public String toString() {
        com.google.gson.Gson gson = new GsonBuilder().registerTypeAdapter(DeviceState.class, new
                DeviceStateSerializer()).create();
        return gson.toJson(this);
    }
    private void updateService() {
        this.updateService(this);
    }
    private void updateService(DeviceState state) {
        Call<String> call = this.service.sendMailbox(state);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }
}
