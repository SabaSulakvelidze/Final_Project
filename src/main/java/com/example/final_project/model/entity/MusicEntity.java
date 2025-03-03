package com.example.final_project.model.entity;

import com.example.final_project.model.enums.MusicGenre;
import com.example.final_project.model.request.MusicRequest;
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
@Table(name = "music_catalog")
public class MusicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "music_id")
    private Long id;

    @Column(name = "music_name", nullable = false, unique = true)
    private String musicName;

    @Column(name = "genre", nullable = false)
    @Enumerated(EnumType.STRING)
    private MusicGenre genre;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private AlbumEntity album;

    @ManyToMany(mappedBy = "musicList")
    private List<PlaylistEntity> playlists = new ArrayList<>();

    public static MusicEntity toMusicEntity(MusicRequest musicRequest, UserEntity author, AlbumEntity album) {
        return MusicEntity.builder()
                .musicName(musicRequest.getMusicName())
                .genre(musicRequest.getGenre())
                .author(author)
                .album(album)
                .build();
    }
}
