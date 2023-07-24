package com.example.spoot_taxi_front.network.api;

import com.example.spoot_taxi_front.network.dto.UserDto;
import com.example.spoot_taxi_front.network.dto.requests.LoginRequest;
import com.example.spoot_taxi_front.network.dto.responses.JoinResponse;
import com.example.spoot_taxi_front.network.dto.responses.LoginResponse;
import com.example.spoot_taxi_front.network.dto.responses.UploadImageResponse;
import com.example.spoot_taxi_front.network.dto.responses.UserSaveResponse;
import com.example.spoot_taxi_front.network.dto.responses.EmailVerificationResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AuthApi {

    //로그인
    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    //회원가입
    @POST("api/auth/join")
    Call<JoinResponse> join(@Body UserDto joinDto);
    //유저정보수정
    @PUT("api/auth/users/{email}")
    Call<UserSaveResponse> updateUser(@Path("email") String email, @Body UserDto updateDto);

    //유저 비밀번호 수정
    @PUT("api/auth/users/{email}/password")
    Call<UserSaveResponse> updateUserPassword(@Path("email") String email, @Body UserDto updateDto);

    //이메일 중복 확인 (가입된 이메일인지 확인)
    @GET("/api/auth/check-duplicate/{email}")
    Call<Boolean> checkDuplicateEmail(@Path("email") String email);

    //이메일 인증 for Join: 가입되어있지 않은 이메일이여도 인증메일 전송
    @GET("/api/auth/join/email-verification")
    Call<EmailVerificationResponse> sendVerificationEmailForJoin(@Query("email") String email);

    //이메일 인증 for update: 가입되어 있는 이메일만 인증메일 전송
    @GET("/api/auth/update/email-verification")
    Call<EmailVerificationResponse> sendVerificationEmailForUpdate(@Query("email") String email);

    //프로필 이미지 업로드
    @Multipart
    @POST("/api/images/profile-image/upload")
    Call<UploadImageResponse> uploadImage(@Part MultipartBody.Part imagePart);


}
