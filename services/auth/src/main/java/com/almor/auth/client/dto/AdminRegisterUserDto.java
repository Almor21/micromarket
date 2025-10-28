package com.almor.auth.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminRegisterUserDto {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    private boolean enabled;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be at most 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be at most 50 characters")
    private String lastName;

    @NotEmpty(message = "At least one credential is required")
    private List<CredentialDto> credentials;

    @Data
    @AllArgsConstructor
    public static class CredentialDto {

        @NotBlank(message = "Credential type is required")
        private String type;

        @NotBlank(message = "Password value is required")
        @Size(min = 4, max = 100, message = "Password must be between 4 and 100 characters")
        private String value;

        private boolean temporary;
    }
}
