package com.example.final_project.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumRequest {
    @NotNull
    @Size(min = 2,max = 64, message = "albumName length must be between 2-64 characters")
    private String albumName;

    @Builder.Default
    private Set<@NotNull @Positive(message = "ID must be positive")Long> musicIdList = new HashSet<>();
}
