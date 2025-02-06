package org.example.final_project.model.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.final_project.model.enums.Role;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "userName can not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "userName must contain only latin letters or numbers, no spaces")
    @Size(min = 2,max = 64, message = "userName size must be between 2-64 characters")
    private String userName;

    @NotBlank(message = "Password can not be empty")
    @Size(min = 8,max = 24, message = "Password size must be between 8-24 characters")
    private String password;

    @NotNull(message = "role can not be null")
    @Enumerated(EnumType.STRING)
    private Role role;
}
