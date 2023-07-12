package com.example.spoot_taxi_front.network.dto.responses;


public class VerificationResponse {
    private Boolean sended;
    private Integer verificationCode;

    public Boolean getSended() {
        return sended;
    }

    public Integer getVerificationCode() {
        return verificationCode;
    }
}
