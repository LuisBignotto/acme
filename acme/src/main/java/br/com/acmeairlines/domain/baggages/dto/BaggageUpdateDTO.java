package br.com.acmeairlines.domain.baggages.dto;

import br.com.acmeairlines.domain.baggages.model.Status;

public record BaggageUpdateDTO(Status status, String lastSeenLocation, String flightId) {}
