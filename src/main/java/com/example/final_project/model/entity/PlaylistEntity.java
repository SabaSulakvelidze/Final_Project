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
@Table(name = "playlists")
public class PlaylistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "playlist_id")
    private Long id;

    private String playlistName;

    private String playlistDescription;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @ManyToMany
    @JoinTable(
            name = "playlist_music",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "music_id")
    )
    private List<MusicEntity> musicList = new ArrayList<>();

    public static PlaylistEntity toPlayListEntity(PlaylistEntity playlistEntity, UserEntity owner) {
        return PlaylistEntity.builder()
                .playlistName(playlistEntity.getPlaylistName())
                .playlistDescription(playlistEntity.getPlaylistDescription())
                .owner(owner)
                .build();
    }
}
