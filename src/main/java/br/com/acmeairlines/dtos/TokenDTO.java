package br.com.acmeairlines.dtos;

public record TokenDTO(
        Long id,
        String token,
        String role
) {}