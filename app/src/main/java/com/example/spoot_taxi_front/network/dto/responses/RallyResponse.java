package com.example.spoot_taxi_front.network.dto.responses;

import com.example.spoot_taxi_front.network.dto.RallyInformationDto;

public class RallyResponse {
    private Boolean success;
    private String message;
    private RallyInformationDto rallyInformationDto;

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public RallyInformationDto getRallyInformationDto() {
        return rallyInformationDto;
    }
}
