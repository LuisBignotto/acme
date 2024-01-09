package br.com.acmeairlines.domain.baggages;

public record BaggageDataRecord(Long id, Long userId, String tag, String color, Double weight, String status, String lastSeenLocation, Long flight) {
    public BaggageDataRecord(BaggageModel baggage) {
        this(baggage.getId(), baggage.getUserId(), baggage.getTag(), baggage.getColor(), baggage.getWeight(), baggage.getStatus(), baggage.getLastSeenLocation(), baggage.getFlightId());
    }
}
