package br.com.acmeairlines.domain.flights.dto;

import br.com.acmeairlines.domain.flights.model.FlightModel;

import java.time.LocalDateTime;

public record FlightDataDTO(String id, String flightNumber, LocalDateTime departureDate, LocalDateTime arrivalDate, String departureAirport, String arrivalAirport) {
    public FlightDataDTO(FlightModel flight){
        this(flight.getId(), flight.getFlightNumber(), flight.getDepartureDate(), flight.getArrivalDate(), flight.getDepartureAirport(), flight.getArrivalAirport());
    }
}
