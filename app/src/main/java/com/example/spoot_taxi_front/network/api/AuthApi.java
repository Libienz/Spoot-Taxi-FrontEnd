package com.example.spoot_taxi_front.network.api;

import com.example.spoot_taxi_front.network.dto.requests.LoginRequest;
import com.example.spoot_taxi_front.network.dto.responses.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    //회원가입

    //유저정보수정


}
