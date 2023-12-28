package br.com.acmeairlines.baggages;

import br.com.acmeairlines.flights.FlightData;
import br.com.acmeairlines.flights.FlightUpdateData;
import jakarta.validation.Valid;

public record BaggageUpdateData(
        String status,
        String lastSeenLocation,
        FlightUpdateData flight
) {
}
