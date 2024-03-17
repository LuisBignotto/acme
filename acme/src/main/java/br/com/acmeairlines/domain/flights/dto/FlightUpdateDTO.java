package br.com.acmeairlines.domain.flights.dto;

import java.time.LocalDateTime;

public record FlightUpdateDTO(LocalDateTime departureDate, LocalDateTime arrivalDate, String departureAirport, String arrivalAirport) {}
