package br.com.acmeairlines.domain.baggages.dto;

import br.com.acmeairlines.domain.baggages.model.Status;

public record BaggageDTO(String id, String userEmail, String tag, String color, Double weight, Status status, String lastSeenLocation, String flightId) {
    public BaggageDTO(String userEmail, String tag, String color, Double weight, Status status, String lastSeenLocation, String flightId){
        this("", userEmail, tag, color, weight, status, lastSeenLocation, flightId);
    }
}
