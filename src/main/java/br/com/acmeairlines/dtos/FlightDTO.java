package br.com.acmeairlines.dtos;

import java.time.LocalDateTime;

public record FlightDTO(
        Long id,
        String tag,
        LocalDateTime departureDate,
        LocalDateTime arrivalDate,
        String departureAirport,
        String arrivalAirport,
        String status,
        String airplaneModel
) {}