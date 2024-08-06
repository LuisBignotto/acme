package br.com.acmeairlines.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record FlightCreateDTO(
        @NotBlank String tag,
        @NotNull LocalDateTime departureDate,
        @NotNull LocalDateTime arrivalDate,
        @NotBlank String departureAirport,
        @NotBlank String arrivalAirport,
        @NotBlank String status,
        @NotBlank String airplaneModel
) {}