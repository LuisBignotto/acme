package br.com.acmeairlines.flights;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record FlightData(
        @NotBlank(message = "O número do voo não pode estar vazio.")
        String flightNumber,
        @NotNull(message = "A data e hora de partida não podem estar vazias.")
        LocalDateTime departureDate,
        @NotNull(message = "A data e hora de chegada não podem estar vazias.")
        LocalDateTime arrivalDate,
        @NotBlank(message = "O aeroporto de partida não pode estar vazio.")
        String departureAirport,
        @NotBlank(message = "O aeroporto de chegada não pode estar vazio.")
        String arrivalAirport
) {
}
