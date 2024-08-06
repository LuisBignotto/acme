package br.com.acmeairlines.dtos;

import java.math.BigDecimal;
import java.util.List;

public record BaggageDTO(
        Long id,
        Long userId,
        String tag,
        String color,
        BigDecimal weight,
        StatusDTO status,
        String lastLocation,
        Long flightId,
        List<BaggageTrackerDTO> trackers
) {}