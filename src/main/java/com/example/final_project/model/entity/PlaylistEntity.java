package com.example.final_project.model.entity;

import com.example.final_project.model.request.PlaylistRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "playlists", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"playlist_name", "owner_id"})
})
public class PlaylistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "playlist_id")
    private Long id;

    @Column(name = "playlist_name")
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
    @Builder.Default
    private Set<MusicEntity> musicList = new HashSet<>();

    public static PlaylistEntity toPlayListEntity(PlaylistRequest playlistRequest, UserEntity owner) {
        return PlaylistEntity.builder()
                .playlistName(playlistRequest.getPlaylistName())
                .playlistDescription(playlistRequest.getPlaylistDescription())
                .owner(owner)
                .build();
    }
}
