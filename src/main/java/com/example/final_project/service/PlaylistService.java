package com.example.final_project.service;

import com.example.final_project.exception.CustomException;
import com.example.final_project.model.entity.PlaylistEntity;
import com.example.final_project.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository playlistRepository;

    public PlaylistEntity save(PlaylistEntity playlistEntity) {
        return playlistRepository.save(playlistEntity);
    }

    public Page<PlaylistEntity> findAllPlaylistsByName(String playlistName, Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return playlistRepository.findPlaylistEntitiesByPlaylistNameContaining(playlistName, PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy)));
    }

    public Page<PlaylistEntity> findAllPlaylists(Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return playlistRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy)));
    }

    public PlaylistEntity findPlaylistById(Long playlistId) {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "playlist with id %d was not found".formatted(playlistId)));
    }

    @Transactional
    public PlaylistEntity updatePlaylistById(Long playlistId, PlaylistEntity playlistEntity) {
        return playlistRepository.findById(playlistId).map(existingPlaylist -> {
            if (playlistEntity.getPlaylistName() != null) existingPlaylist.setPlaylistName(playlistEntity.getPlaylistName());
            if (playlistEntity.getPlaylistDescription() != null) existingPlaylist.setPlaylistDescription(playlistEntity.getPlaylistDescription());
            if (playlistEntity.getOwner() != null) existingPlaylist.setOwner(playlistEntity.getOwner());
            if (playlistEntity.getMusicList() != null && !playlistEntity.getMusicList().isEmpty()) existingPlaylist.setMusicList(playlistEntity.getMusicList());
            return existingPlaylist;
        }).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "playlist with id %d was not found".formatted(playlistId)));
    }

    public void deletePlaylistIdById(Long playlistId) {
        playlistRepository.deleteById(playlistId);
    }
}
