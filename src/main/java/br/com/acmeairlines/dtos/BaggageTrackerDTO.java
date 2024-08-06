package br.com.acmeairlines.dtos;

public record BaggageTrackerDTO(
        Long id,
        Long baggageId,
        Long trackerUserId
) {}
