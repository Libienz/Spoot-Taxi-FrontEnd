package com.example.spoot_taxi_front.network.api;

import com.example.spoot_taxi_front.network.dto.requests.MatchCancelRequest;
import com.example.spoot_taxi_front.network.dto.requests.MatchingRequest;
import com.example.spoot_taxi_front.network.dto.responses.MatchCancelResponse;
import com.example.spoot_taxi_front.network.dto.responses.MatchingResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MatchingApi {

    @POST("/api/match/request")
    Call<MatchingResponse> requestMatch(@Body MatchingRequest matchingRequest);

    @POST("/api/match/cancel-request")
    Call<MatchCancelResponse> cancelMatching(@Body MatchCancelRequest matchCancelRequest);
}
