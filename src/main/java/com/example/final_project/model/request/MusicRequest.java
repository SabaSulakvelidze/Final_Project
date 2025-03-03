package com.example.final_project.model.request;

import com.example.final_project.model.enums.MusicGenre;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MusicRequest {
    @NotBlank(message = "musicName can not be empty")
    @Size(min = 2,max = 64, message = "musicName length must be between 2-64 characters")
    private String musicName;

    @NotNull(message = "genre can not be null")
    @Enumerated(EnumType.STRING)
    private MusicGenre genre;

    @Positive(message = "id must be positive")
    private Long authorId;

    @Positive(message = "id must be positive")
    private Long albumId;
}
