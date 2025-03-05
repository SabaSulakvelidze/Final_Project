package com.example.final_project.model.entity;

import com.example.final_project.model.request.AlbumRequest;
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
@Table(name = "albums")
public class AlbumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "album_id")
    private Long id;

    @Column(name = "album_name", nullable = false, unique = true)
    private String albumName;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @OneToMany(mappedBy = "album")
    @Builder.Default
    private Set<MusicEntity> musicList = new HashSet<>();

    public static AlbumEntity toAlbumEntity(AlbumRequest albumRequest,UserEntity owner) {
        return AlbumEntity.builder()
                .albumName(albumRequest.getTitle())
                .owner(owner)
                .build();
    }
}
