package com.example.spoot_taxi_front.network.retrofit;

import com.example.spoot_taxi_front.network.api.AuthApi;
import com.example.spoot_taxi_front.network.api.RallyApi;

public class ApiManager {
    private static ApiManager instance;
    private AuthApi authApi;
    private RallyApi rallyApi;

    private ApiManager() {
        // 인스턴스 생성 시 API 인터페이스 구현체 초기화
        authApi = ApiClient.createAuthApi();
        rallyApi = ApiClient.createRallyApi();
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
