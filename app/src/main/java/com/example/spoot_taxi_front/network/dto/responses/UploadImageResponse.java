package com.example.spoot_taxi_front.network.dto.responses;

public class UploadImageResponse {
    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    private Boolean success;
    private String message;
    private String imageUrl;

}
