package br.com.acmeairlines.flights;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record FlightUpdateData(
        LocalDateTime departureDate,
        LocalDateTime arrivalDate,
        String departureAirport,
        String arrivalAirport
) {
}
