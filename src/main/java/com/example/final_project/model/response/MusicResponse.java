package com.example.final_project.model.response;

import com.example.final_project.model.entity.MusicEntity;
import com.example.final_project.model.entity.PlaylistEntity;
import com.example.final_project.model.enums.MusicGenre;
import lombok.*;

import java.util.HashMap;
import java.util.stream.Collectors;

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
    private HashMap<Long, String> playlists = new HashMap<>();

    public static MusicResponse toMusicResponse(MusicEntity musicEntity) {
        HashMap<Long, String> plMap = musicEntity.getPlaylists() != null ? musicEntity.getPlaylists().stream()
                .collect(Collectors.toMap(PlaylistEntity::getId, PlaylistEntity::getPlaylistName,
                        (existing, replacement) -> existing, HashMap::new)) : null;
        String albumName = musicEntity.getAlbum() != null ? musicEntity.getAlbum().getAlbumName() : null;
        return MusicResponse.builder()
                .musicId(musicEntity.getId())
                .musicName(musicEntity.getMusicName())
                .musicGenre(musicEntity.getMusicGenre())
                .authorName(musicEntity.getAuthor().getUsername())
                .albumName(albumName)
                .playlists(plMap)
                .build();

    }
}
