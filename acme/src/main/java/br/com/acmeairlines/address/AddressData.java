package br.com.acmeairlines.address;

public record AddressData(
        String street,
        String neighborhood,
        String zipcode,
        String number,
        String complement,
        String city,
        String state
        ) { }
