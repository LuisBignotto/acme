package br.com.acmeairlines.domain.flights.dto;

import java.time.LocalDateTime;

public record FlightDataDTO(String id, String flightNumber, LocalDateTime departureDate, LocalDateTime arrivalDate, String departureAirport, String arrivalAirport) {}
