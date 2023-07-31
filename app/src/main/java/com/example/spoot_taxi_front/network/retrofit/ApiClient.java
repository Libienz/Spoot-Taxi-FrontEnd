package com.example.spoot_taxi_front.network.retrofit;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.spoot_taxi_front.network.api.AuthApi;
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

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


//    private static final String BASE_URL = "http://192.168.219.109:8080/";
    private static final String BASE_URL = "http://192.168.123.100:8080/";




    public static AuthApi createAuthApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(AuthApi.class);
    }

    public static RallyApi createRallyApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory())
                .build();
        return retrofit.create(RallyApi.class);
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

