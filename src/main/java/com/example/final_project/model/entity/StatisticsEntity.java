package com.example.final_project.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Entity
@Table(name = "statistics")
public class StatisticsEntity {
    @EmbeddedId
    private StatisticsId statisticsId;

    @JsonBackReference
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @JsonBackReference
    @ManyToOne
    @MapsId("musicId")
    @JoinColumn(name = "music_id", nullable = false)
    private MusicEntity music;

    private Integer playCount;
}
