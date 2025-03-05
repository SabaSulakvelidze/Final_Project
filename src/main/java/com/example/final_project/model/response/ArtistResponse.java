package com.example.final_project.model.response;

import com.example.final_project.model.entity.UserEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class ArtistResponse extends UserResponse {
    private HashMap<Long, String> similarArtists = new HashMap<>();

    public static ArtistResponse toArtistResponse(UserEntity userEntity, HashMap<Long, String> similarArtists) {
        return ArtistResponse.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .role(userEntity.getUserRole())
                .status(userEntity.getUserStatus())
                .similarArtists(similarArtists)
                .build();
    }
}
