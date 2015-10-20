package co.vulcanus.dux.service;



import co.vulcanus.dux.model.DeviceState;
import co.vulcanus.dux.model.Pin;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.Call;

/**
 * Created by ryan_turner on 10/17/15.
 */
public interface ApiService {
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @GET("/arduino/digital/{pin}")
    Call<Pin> getPin(@Path("pin") int pin);

    @GET("/arduino/digital/{pin}/{state}")
    Call<Pin> setPin(@Path("pin") int pin, @Query("state") int state);

    @GET("/mailbox/{deviceState}")
    Call<String> sendMailbox(@Path("deviceState") DeviceState deviceState);
}