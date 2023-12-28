package br.com.acmeairlines.flights;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Table(name = "flights")
@Entity(name = "flights")
@NoArgsConstructor
@AllArgsConstructor
public class FlightModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String flightNumber;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private String departureAirport;
    private String arrivalAirport;
    public FlightModel(FlightData data) {
        this.flightNumber = data.flightNumber();
        this.departureDate = data.departureDate();
        this.arrivalDate = data.arrivalDate();
        this.departureAirport = data.departureAirport();
        this.arrivalAirport = data.arrivalAirport();
    }
    public void updateFlight(FlightUpdateData data) {
        if (data.departureDate() != null) {
            this.departureDate = data.departureDate();
        }
        if (data.arrivalDate() != null) {
            this.arrivalDate = data.arrivalDate();
        }
        if (data.departureAirport() != null) {
            this.departureAirport = data.departureAirport();
        }
        if (data.arrivalAirport() != null) {
            this.arrivalAirport = data.arrivalAirport();
        }
    }
}
