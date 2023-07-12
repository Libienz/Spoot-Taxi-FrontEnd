package com.example.spoot_taxi_front.network.dto.requests;

public class VerificationRequest {

    Boolean noFoundThenSend;
    Boolean FoundThenSend;

    public VerificationRequest(Boolean noFoundThenSend, Boolean foundThenSend) {
        this.noFoundThenSend = noFoundThenSend;
        FoundThenSend = foundThenSend;
    }

    public Boolean getNoFoundThenSend() {
        return noFoundThenSend;
    }

    public void setNoFoundThenSend(Boolean noFoundThenSend) {
        this.noFoundThenSend = noFoundThenSend;
    }

    public Boolean getFoundThenSend() {
        return FoundThenSend;
    }

    public void setFoundThenSend(Boolean foundThenSend) {
        FoundThenSend = foundThenSend;
    }
}
