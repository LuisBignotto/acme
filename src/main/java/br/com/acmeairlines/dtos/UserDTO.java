package br.com.acmeairlines.dtos;

public record UserDTO(
        Long id,
        String email,
        String cpf,
        String name,
        String phone,
        AddressDTO address,
        String role
) {}