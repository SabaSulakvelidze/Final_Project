package com.example.final_project.repository;

import com.example.final_project.model.entity.PlaylistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {
    Page<PlaylistEntity> findPlaylistEntitiesByPlaylistNameContaining(String playlistName, Pageable pageable);
}
