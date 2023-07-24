package com.example.spoot_taxi_front.network.dto.responses;


public class EmailVerificationResponse {
    private Boolean success;
    private String message;
    private Integer verificationCode;

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Integer getVerificationCode() {
        return verificationCode;
    }
}
