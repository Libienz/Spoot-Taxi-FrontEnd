package com.example.spoot_taxi_front.network.dto.responses;

public class UploadImageResponse {

    private Boolean success;
    private String message;
    private String imageUrl;

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
