package br.com.acmeairlines.domain.users.dto;

import br.com.acmeairlines.domain.users.model.Role;
import br.com.acmeairlines.domain.users.model.address.AddressData;

public record UserUpdateDTO(String name, String email, String password, String currentPassword, String phone, AddressData address, Role role) {}
