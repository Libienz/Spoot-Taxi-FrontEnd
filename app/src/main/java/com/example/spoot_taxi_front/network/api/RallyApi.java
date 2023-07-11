package com.example.spoot_taxi_front.network.api;

import com.example.spoot_taxi_front.network.dto.RallyInfoDto;
import com.example.spoot_taxi_front.network.dto.requests.LoginRequest;
import com.example.spoot_taxi_front.network.dto.responses.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RallyApi {
    @GET("/api/rally-info")
    Call<RallyInfoDto> getRallyInfo();

}
