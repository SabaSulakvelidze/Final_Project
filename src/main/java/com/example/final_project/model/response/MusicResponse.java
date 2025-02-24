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
    private MusicGenre genre;
    private String authorName;
    private String albumName;
    private HashMap<Long, String> playlists = new HashMap<>();

    public static MusicResponse toMusicResponse(MusicEntity musicEntity) {
        HashMap<Long, String> plMap = musicEntity.getPlaylists().stream()
                .collect(Collectors.toMap(PlaylistEntity::getPlaylistId, PlaylistEntity::getPlaylistName, (existing, replacement) -> existing, HashMap::new));
        return MusicResponse.builder()
                .musicId(musicEntity.getMusicId())
                .musicName(musicEntity.getMusicName())
                .genre(musicEntity.getGenre())
                .authorName(musicEntity.getAuthor().getUsername())
                .albumName(musicEntity.getAlbum().getAlbumName())
                .playlists(plMap)
                .build();

    }
}
