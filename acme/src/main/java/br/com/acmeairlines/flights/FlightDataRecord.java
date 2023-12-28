package br.com.acmeairlines.flights;

import java.time.LocalDateTime;

public record FlightDataRecord(Long id, String flightNumber, LocalDateTime departureDate, LocalDateTime arrivalDate, String departureAirport, String arrivalAirport) {
    public FlightDataRecord(FlightModel flight) {
        this(flight.getId(), flight.getFlightNumber(), flight.getDepartureDate(), flight.getArrivalDate(), flight.getDepartureAirport(), flight.getArrivalAirport());
    }
}
