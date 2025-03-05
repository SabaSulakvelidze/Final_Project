package com.example.final_project.model.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumRequest {
    @NotNull
    @Size(min = 2,max = 64, message = "title length must be between 2-64 characters")
    private String title;

    @Builder.Default
    private Set<@NotNull @Positive(message = "ID must be positive")Long> musicIdList = new HashSet<>();
}
