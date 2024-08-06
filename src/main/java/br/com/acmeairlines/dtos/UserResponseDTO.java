package br.com.acmeairlines.dtos;

import java.util.List;

public record UserResponseDTO (
        Long id,
        String email,
        String cpf,
        String name,
        String phone,
        AddressDTO address,
        String role,
        List<BaggageDTO> baggages
) {}