package br.com.acmeairlines.domain.baggages;

public record BaggageUpdateData(
        Long userId,
        String status,
        String lastSeenLocation,
        Long flightId
) {
}
