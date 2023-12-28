package br.com.acmeairlines.users;

import br.com.acmeairlines.address.AddressData;
import jakarta.validation.constraints.NotNull;

public record UserUpdateData(
        @NotNull
        Long id,
        String name,
        String email,
        String password,
        String phone,
        AddressData address
) { }
