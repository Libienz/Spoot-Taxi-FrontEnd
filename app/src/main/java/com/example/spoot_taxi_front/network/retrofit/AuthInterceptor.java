package com.example.spoot_taxi_front.network.retrofit;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private String jwtToken;

    public AuthInterceptor(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder newRequestBuilder = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + jwtToken);

        Request newRequest = newRequestBuilder.build();
        return chain.proceed(newRequest);

    }

}
