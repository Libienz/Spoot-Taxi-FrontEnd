package com.example.spoot_taxi_front.network.dto;

import java.time.LocalDateTime;
import java.util.List;


public class RallyInformationDto {


    private String comment;
    private LocalDateTime date;

    private List<RallyDetailsDto> rallyDetailsDtoList;

    public static class RallyDetailsDto {
        private LocalDateTime startTime;

        private LocalDateTime endTime;
        private String location;
        private String rallyScale;
        private String locationDetail;

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

        public String getLocationDetail() {
            return locationDetail;
        }
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }

    public List<RallyDetailsDto> getRallyDetailsDtoList() {
        return rallyDetailsDtoList;
    }
}
