package br.com.acmeairlines.baggages;

import br.com.acmeairlines.flights.FlightData;
import br.com.acmeairlines.flights.FlightUpdateData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record BaggageUpdateData(
        @NotNull
        Long id,
        String status,
        String lastSeenLocation,
        FlightUpdateData flight
) {
}
