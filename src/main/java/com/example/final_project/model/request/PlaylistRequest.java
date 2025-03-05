package com.example.final_project.model.request;

import jakarta.validation.constraints.NotBlank;
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
public class PlaylistRequest {
    @NotBlank(message = "playlistName can not be empty")
    @Size(min = 2, max = 64, message = "playlistName length must be between 2-64 characters")
    private String playlistName;

    @Size(min = 20, max = 256, message = "Description must contain between 20-256 characters")
    private String playlistDescription;

    @Builder.Default
    private Set<@NotNull @Positive(message = "ID must be positive")Long> musicIdList = new HashSet<>();
}
