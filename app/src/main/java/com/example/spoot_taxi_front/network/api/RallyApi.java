package com.example.spoot_taxi_front.network.api;

import com.example.spoot_taxi_front.network.dto.RallyInformationDto;
import com.example.spoot_taxi_front.network.dto.responses.RallyResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RallyApi {
    @GET("/api/rally-info")
    Call<RallyResponse> getRallyInfo();

}
