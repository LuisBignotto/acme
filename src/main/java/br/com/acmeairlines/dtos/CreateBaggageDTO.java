package br.com.acmeairlines.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record CreateBaggageDTO(
        @NotNull Long userId,
        @NotNull @Size(min = 1, max = 10) String tag,
        @NotNull @Size(min = 1, max = 50) String color,
        @NotNull BigDecimal weight,
        @NotNull Long statusId,
        @NotNull @Size(min = 1, max = 100) String lastLocation,
        @NotNull Long flightId,
        List<CreateBaggageTrackerDTO> trackers
) {}
