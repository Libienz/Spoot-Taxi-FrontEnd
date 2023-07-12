package com.example.spoot_taxi_front.network.retrofit;

import com.example.spoot_taxi_front.network.api.AuthApi;
import com.example.spoot_taxi_front.network.api.RallyApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private static final String BASE_URL = "http://192.168.219.109:8080/";
    private static ApiManager instance;
    private AuthApi authApi;
    private RallyApi rallyApi;

    private ApiManager() {
        // 인스턴스 생성 시 API 인터페이스 구현체 초기화
        authApi = createAuthApi();
        rallyApi = ApiClient.createRallyApi();
    }

    public static AuthApi createAuthApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(AuthApi.class);
    }

    public static synchronized ApiManager getInstance() {
        if (instance == null) {
            instance = new ApiManager();
        }
        return instance;
    }

    public AuthApi getAuthApi() {
        return authApi;
    }

    public RallyApi getRallyApi() {
        return rallyApi;
    }
}
