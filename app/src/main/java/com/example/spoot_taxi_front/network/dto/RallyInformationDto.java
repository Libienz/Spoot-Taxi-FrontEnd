package com.example.spoot_taxi_front.network.dto;

import java.time.LocalDateTime;
import java.util.List;


public class RallyInformationDto {

    private LocalDateTime date;

    private List<RallyDetailsDto> rallyDetailsDtoList;


    public static class RallyDetailsDto {
        private LocalDateTime startTime;

        private LocalDateTime endTime;
        private String location;
        private String rallyScale;
        private String jurisdiction;

        public LocalDateTime getStartTime() {
            return startTime;
        }

        public LocalDateTime getEndTime() {
            return endTime;
        }

        public String getLocation() {
            return location;
        }
        public String getRallyScale() {
            return rallyScale;
        }

        public String getJurisdiction() {
            return jurisdiction;
        }
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<RallyDetailsDto> getRallyDetailsList() {
        return rallyDetailsDtoList;
    }
}