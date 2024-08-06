package br.com.acmeairlines.dtos;

public record MessageCreateDTO(
        Long senderId,
        String message
) {}
