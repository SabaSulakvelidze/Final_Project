package com.example.final_project.model.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumRequest {
    @NotNull
    @Size(min = 2,max = 64, message = "title length must be between 2-64 characters")
    private String title;

    private List<@NotNull @Positive(message = "ID must be positive")Long> musicIdList = new ArrayList<>();
}
