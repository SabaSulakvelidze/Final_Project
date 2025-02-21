package com.example.final_project.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionBody {
    private List<String> message;
    private String endpoint;
    private LocalDateTime timestamp;

}
