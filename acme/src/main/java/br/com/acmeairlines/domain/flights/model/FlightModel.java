package br.com.acmeairlines.domain.flights.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Table(name = "flights")
@Entity(name = "flights")
@NoArgsConstructor
@AllArgsConstructor
public class FlightModel {

    @Id
    private String id;
    private String flightNumber;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private String departureAirport;
    private String arrivalAirport;

}
