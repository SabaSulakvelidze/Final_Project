package com.example.final_project.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "albums")
public class AlbumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "album_id")
    private Long id;

    @Column(name = "album_name", nullable = false, unique = true)
    private String albumName;

    @OneToMany(mappedBy = "album")
    private List<MusicEntity> musicList = new ArrayList<>();
}
