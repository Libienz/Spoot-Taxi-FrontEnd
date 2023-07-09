package com.example.spoot_taxi_front.network.api;

import com.example.spoot_taxi_front.network.dto.requests.LoginRequest;
import com.example.spoot_taxi_front.network.dto.responses.LoginResponse;
import com.example.spoot_taxi_front.network.dto.responses.VerificationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AuthApi {

    //로그인
    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    //회원가입

    //유저정보수정

    //이메일 중복 확인
    @GET("/api/auth/check-duplicate/{email}")
    Call<Boolean> checkDuplicateEmail(@Path("email") String email);

    //이메일 인증
    @GET("/api/auth/email-verification")
    Call<VerificationResponse> sendVerificationEmail(@Query("email") String email);


}
