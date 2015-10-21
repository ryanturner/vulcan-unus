package co.vulcanus.dux.service;

import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;

import co.vulcanus.dux.util.Constants;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by ryan_turner on 10/20/15.
 */
public class RestClient {
    private static RestClient ourInstance = null;
    private static String baseUrl;
    private static String username;
    private static String password;

    private ApiService service;

    public ApiService getService() {
        return service;
    }

    public static void setBaseUrl(String baseUrl) {
        RestClient.baseUrl = baseUrl;
    }

    public static void setUsername(String username) {
        RestClient.username = username;
    }

    public static void setPassword(String password) {
        RestClient.password = password;
    }

    public void setService(ApiService service) {
        this.service = service;
    }

    public static void destroyInstance() {
        ourInstance = null;
    }

    public static RestClient getInstance() {
        if(ourInstance == null) {
            ourInstance = new RestClient();
            ourInstance.setupClient(baseUrl, username, password);
        }
        return ourInstance;
    }

    private RestClient() {
    }
    private void setupClient(String baseUrl, final String username, final String password) {
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Authorization", Credentials.basic(username, password))
                        .method(original.method(), original.body())
                        .build();

                com.squareup.okhttp.Response response = chain.proceed(request);

                return response;
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        this.service = retrofit.create(ApiService.class);
    }
}
