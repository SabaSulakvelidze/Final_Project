package com.example.final_project.model.response;

import com.example.final_project.model.entity.MusicEntity;
import com.example.final_project.model.entity.PlaylistEntity;
import lombok.*;

import java.util.HashMap;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistResponse {
    private Long playlistId;
    private String playlistName;
    private String playlistDescription;
    private String ownerName;
    private HashMap<Long, String> musicList = new HashMap<>();

    public static PlaylistResponse toPlaylistResponse(PlaylistEntity playlistEntity) {
        HashMap<Long, String> plMap = playlistEntity.getMusicList().stream()
                .collect(Collectors.toMap(MusicEntity::getId, MusicEntity::getMusicName, (existing, replacement) -> existing, HashMap::new));
        return PlaylistResponse.builder()
                .playlistId(playlistEntity.getId())
                .playlistName(playlistEntity.getPlaylistName())
                .playlistDescription(playlistEntity.getPlaylistDescription())
                .musicList(plMap)
                .build();

    }
}

