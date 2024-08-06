package br.com.acmeairlines.dtos;

import jakarta.validation.constraints.NotNull;

public record CreateBaggageTrackerDTO(
        @NotNull Long trackerUserId
) {}