package com.example.final_project.model.response.statistics;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class PlayCountStat {
    private Long musicId;
    private String musicName;
    private Integer playCount;
}
