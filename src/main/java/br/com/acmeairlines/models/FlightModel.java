package br.com.acmeairlines.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String tag;

    @NotNull
    private LocalDateTime departureDate;

    @NotNull
    private LocalDateTime arrivalDate;

    @NotNull
    private String departureAirport;

    @NotNull
    private String arrivalAirport;

    @NotNull
    private String status;

    @NotNull
    private String airplaneModel;
}