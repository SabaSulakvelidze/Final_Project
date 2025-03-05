package com.example.final_project.model.response;

import com.example.final_project.model.entity.MusicEntity;
import com.example.final_project.model.enums.MusicGenre;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MusicResponse {
    private Long musicId;
    private String musicName;
    private MusicGenre musicGenre;
    private String authorName;
    private String albumName;

    public static MusicResponse toMusicResponse(MusicEntity musicEntity) {
        String albumName = musicEntity.getAlbum() != null ? musicEntity.getAlbum().getAlbumName() : null;
        return MusicResponse.builder()
                .musicId(musicEntity.getId())
                .musicName(musicEntity.getMusicName())
                .musicGenre(musicEntity.getMusicGenre())
                .authorName(musicEntity.getAuthor().getUsername())
                .albumName(albumName)
                .build();

    }
}
