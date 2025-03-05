package com.example.final_project.model.response.statistics;

import com.example.final_project.model.response.UserResponse;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StatisticsResponse {
    private UserResponse user;
    private Set<PlayCountStat> playCountStats;
}
