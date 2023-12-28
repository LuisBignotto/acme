package br.com.acmeairlines.flights;

import java.time.LocalDateTime;

public record FlightUpdateData(
        LocalDateTime departureDate,
        LocalDateTime arrivalDate,
        String departureAirport,
        String arrivalAirport
) {
}
