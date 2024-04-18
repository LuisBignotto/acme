package br.com.acmeairlines.domain.users.dto;

import br.com.acmeairlines.domain.users.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRegisterDTO(
        @NotBlank(message = "Name cannot be empty.")
        String name,
        @Email(message = "Email must be a valid email address.")
        @NotBlank(message = "Email cannot be empty.")
        String email,
        @NotBlank(message = "CPF cannot be empty.")
        String cpf,
        @NotBlank(message = "Password cannot be empty.")
        String password,
        @NotNull(message = "Role cannot be null.")
        Role role,
        @NotNull(message = "Active cannot be null.")
        Boolean active
) {}
