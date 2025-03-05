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
    private String ownerName;
    @Builder.Default
    private HashMap<Long, String> musicList = new HashMap<>();

    public static AlbumResponse toAlbumResponse(AlbumEntity albumEntity) {
        HashMap<Long, String> musicMap = albumEntity.getMusicList() != null ? albumEntity.getMusicList().stream()
                .collect(Collectors.toMap(MusicEntity::getId, MusicEntity::getMusicName,
                        (existing, replacement) -> existing, HashMap::new)) : null;
        return AlbumResponse.builder()
                .albumId(albumEntity.getId())
                .title(albumEntity.getAlbumName())
                .ownerName(albumEntity.getOwner().getUsername())
                .musicList(musicMap)
                .build();
    }
}
