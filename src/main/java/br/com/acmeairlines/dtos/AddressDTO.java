package br.com.acmeairlines.dtos;

public record AddressDTO(
        Long id,
        String street,
        String neighborhood,
        String zipcode,
        String number,
        String complement,
        String city,
        String state
) {}