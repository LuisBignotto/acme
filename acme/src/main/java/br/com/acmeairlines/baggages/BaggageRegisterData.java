package br.com.acmeairlines.baggages;

import br.com.acmeairlines.flights.FlightData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BaggageRegisterData(
        @NotNull(message = "O id de usuário não pode estar vazio.")
        Long userId,
        @NotBlank(message = "A tag não pode estar vazia.")
        String tag,
        @NotBlank(message = "A cor não pode estar vazia.")
        String color,
        @NotNull(message = "O peso não pode estar vazio.")
        double weight,
        @NotBlank(message = "O status não pode estar vazio.")
        String status,
        @NotBlank(message = "O último local não pode estar vazio.")
        String lastSeenLocation,
        @Valid
        @NotNull(message = "O voo não pode estar vazio.")
        FlightData flight
) {
}
