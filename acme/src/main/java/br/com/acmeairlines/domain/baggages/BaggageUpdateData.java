package br.com.acmeairlines.domain.baggages;

public record BaggageUpdateData(
        String status,
        String lastSeenLocation,
        Long flightId
) {
}
