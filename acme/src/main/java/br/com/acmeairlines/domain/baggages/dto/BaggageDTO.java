package br.com.acmeairlines.domain.baggages.dto;

import br.com.acmeairlines.domain.baggages.model.Status;

public record BaggageDTO(String id, String userId, String tag, String color, Double weight, Status status, String lastSeenLocation, String flightId) {}
