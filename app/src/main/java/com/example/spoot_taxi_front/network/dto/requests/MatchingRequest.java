package com.example.spoot_taxi_front.network.dto.requests;

public class MatchingRequest {
    private String email;
    private String deviceToken;
    private double latitude;
    private double longitude;

    public MatchingRequest(String email, String deviceToken, double latitude, double longitude) {
        this.email = email;
        this.deviceToken = deviceToken;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
