package com.example.spoot_taxi_front.network.retrofit;

import com.example.spoot_taxi_front.network.api.AuthApi;
import com.example.spoot_taxi_front.network.api.ChatApi;
import com.example.spoot_taxi_front.network.api.MatchingApi;
import com.example.spoot_taxi_front.network.api.RallyApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private static final String BASE_URL = "http://192.168.219.110:8080/";
//    private static final String BASE_URL = "http://192.168.123.100:8080/";

    private static ApiManager instance;

    private ApiManager() {
    }

    public static synchronized ApiManager getInstance() {
        if (instance == null) {
            instance = new ApiManager();
        }
        return instance;
    }

    public AuthApi createAuthApi(String jwtToken) {
        // AuthInterceptor를 생성하여 토큰을 추가한 OkHttpClient를 생성
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new AuthInterceptor(jwtToken));

        httpClient.connectTimeout(30, TimeUnit.SECONDS); // 연결 타임아웃 설정
        httpClient.readTimeout(30, TimeUnit.SECONDS); // 읽기 타임아웃 설정
        httpClient.writeTimeout(30, TimeUnit.SECONDS); // 쓰기 타임아웃 설정

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofit.create(AuthApi.class);
    }

    public RallyApi createRallyApi(String jwtToken) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new AuthInterceptor(jwtToken));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory())
                .client(httpClient.build())
                .build();

        return retrofit.create(RallyApi.class);
    }

    public MatchingApi createMatchingApi(String jwtToken) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new AuthInterceptor(jwtToken));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory())
                .client(httpClient.build())
                .build();
        return retrofit.create(MatchingApi.class);
    }


    public static ChatApi createChatApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory())
                .build();
        return retrofit.create(ChatApi.class);
    }


    // String을 LocalDateTime 으로 변형하는걸 등록한다.
    public static GsonConverterFactory gsonConverterFactory(){
        Gson gson = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                        @Override
                        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                            return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                        }
                    })
                    .create();
        }
        return GsonConverterFactory.create(gson);
    }




}
