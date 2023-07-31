package com.example.spoot_taxi_front.network.retrofit;

import com.example.spoot_taxi_front.network.api.AuthApi;
import com.example.spoot_taxi_front.network.api.RallyApi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private static final String BASE_URL = "http://192.168.219.110:8080/";
    private static ApiManager instance;

    private ApiManager() {
    }

    public AuthApi createAuthApi(String jwtToken) {
        // AuthInterceptor를 생성하여 토큰을 추가한 OkHttpClient를 생성
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new AuthInterceptor(jwtToken));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofit.create(AuthApi.class);
    }
    public static synchronized ApiManager getInstance() {
        if (instance == null) {
            instance = new ApiManager();
        }
        return instance;
    }

}
