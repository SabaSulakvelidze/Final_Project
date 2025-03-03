package com.example.final_project.model.response;

import com.example.final_project.model.entity.AlbumEntity;
import com.example.final_project.model.entity.MusicEntity;
import lombok.*;

import java.util.HashMap;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumResponse {
    private Long albumId;
    private String title;
    private HashMap<Long,String> musicList = new HashMap<>();

    public static AlbumResponse toAlbumResponse(AlbumEntity albumEntity) {
        HashMap<Long, String> musicMap = albumEntity.getMusicList().stream()
                .collect(Collectors.toMap(MusicEntity::getId, MusicEntity::getMusicName, (existing, replacement) -> existing, HashMap::new));
        return AlbumResponse.builder()
                .albumId(albumEntity.getId())
                .title(albumEntity.getAlbumName())
                .musicList(musicMap)
                .build();
    }
}
