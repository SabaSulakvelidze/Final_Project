package com.example.final_project.model.response.statistics;

import com.example.final_project.model.response.MusicResponse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class PlayCountStat {
    private MusicResponse music;
    private Integer playCount;
}
