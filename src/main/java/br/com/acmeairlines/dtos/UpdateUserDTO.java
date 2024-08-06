package br.com.acmeairlines.dtos;

public record UpdateUserDTO(
        String name,
        String email,
        String phone,
        String password,
        AddressDTO address
) {}