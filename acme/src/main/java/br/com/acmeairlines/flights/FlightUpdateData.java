package br.com.acmeairlines.flights;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record FlightUpdateData(
        @NotNull
        Long id,
        LocalDateTime departureDate,
        LocalDateTime arrivalDate,
        String departureAirport,
        String arrivalAirport
) {
}
