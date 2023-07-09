package com.example.spoot_taxi_front.network.dto.responses;


public class VerificationResponse {
    private Boolean emailChecked;
    private Integer verificationCode;

    public Boolean getEmailChecked() {
        return emailChecked;
    }

    public Integer getVerificationCode() {
        return verificationCode;
    }
}
