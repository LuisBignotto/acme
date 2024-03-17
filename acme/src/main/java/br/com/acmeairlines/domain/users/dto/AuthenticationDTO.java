package br.com.acmeairlines.domain.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
        @Email(message = "Email must be a valid email address.")
        @NotBlank(message = "Email cannot be empty.")
        String email,
        @NotBlank(message = "Password cannot be empty.")
        String password
) {}
