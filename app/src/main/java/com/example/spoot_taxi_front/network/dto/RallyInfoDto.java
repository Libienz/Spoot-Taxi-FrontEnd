package com.example.spoot_taxi_front.network.dto;

import java.time.LocalDateTime;
import java.util.List;


public class RallyInfoDto {

    private LocalDateTime date;

    private List<RallyDetailsDto> rallyDetailsList;


    public static class RallyDetailsDto {
        private LocalDateTime startTime;

        private LocalDateTime endTime;
        private String location;

        public LocalDateTime getStartTime() {
            return startTime;
        }

        public LocalDateTime getEndTime() {
            return endTime;
        }

        public String getLocation() {
            return location;
        }
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<RallyDetailsDto> getRallyDetailsList() {
        return rallyDetailsList;
    }
}
