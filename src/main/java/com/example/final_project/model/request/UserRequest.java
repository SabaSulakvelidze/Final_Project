package com.example.final_project.model.request;

import com.example.final_project.model.enums.UserRole;
import com.example.final_project.model.enums.UserStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "userName can not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "userName must contain only latin letters or numbers, no spaces")
    @Size(min = 2,max = 64, message = "userName size must be between 2-64 characters")
    private String userName;

    @NotBlank(message = "Password can not be empty")
    @Size(min = 8,max = 24, message = "Password size must be between 8-24 characters")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email cannot be longer than 255 characters")
    private String email;

    @NotNull(message = "role can not be null")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @NotNull(message = "status can not be null")
    @Enumerated(EnumType.STRING)
    private UserStatus status;
}
