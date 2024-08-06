package br.com.acmeairlines.dtos;

public record TicketCreateDTO(
        Long userId,
        String title,
        String description
) {}
