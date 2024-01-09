package br.com.acmeairlines.domain.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRegisterData(
        @NotBlank(message = "O nome não pode estar vazio.")
        String name,
        @Email
        @NotBlank(message = "O email não pode estar vazio.")
        String email,
        @NotBlank(message = "A senha não pode estar vazia.")
        String password,
        @NotNull(message = "O cargo não pode estar vazio.")
        Role role,
        @NotNull
        Boolean active
) { }
