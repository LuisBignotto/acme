package br.com.acmeairlines.domain.users;

import br.com.acmeairlines.domain.address.AddressData;

public record UserUpdateData(
        String name,
        String email,
        String password,
        String phone,
        AddressData address
) {}
