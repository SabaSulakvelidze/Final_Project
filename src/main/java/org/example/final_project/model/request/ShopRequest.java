package org.example.final_project.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopRequest {
    @NotBlank(message = "shopName can not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "shopName must contain only latin letters or numbers")
    @Size(min = 2,max = 64, message = "shopName size must be between 2-64 characters")
    private String name;
}
