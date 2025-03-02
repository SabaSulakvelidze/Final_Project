package com.example.final_project.service;

import com.example.final_project.exception.CustomException;
import com.example.final_project.model.entity.AlbumEntity;
import com.example.final_project.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;

    public AlbumEntity save(AlbumEntity albumEntity) {
        return albumRepository.save(albumEntity);
    }

    public Page<AlbumEntity> findAllAlbumsByName(String albumName, Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return albumRepository.findAlbumEntitiesByAlbumNameContaining(albumName, PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy)));
    }

    public Page<AlbumEntity> findAllAlbums(Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return albumRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy)));
    }

    public AlbumEntity findAlbumById(Long albumId) {
        return albumRepository.findById(albumId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "album with id %d was not found".formatted(albumId)));
    }

    @Transactional
    public AlbumEntity updateAlbumById(Long albumId, AlbumEntity albumEntity) {
        return albumRepository.findById(albumId).map(existingAlbum -> {
            if (albumEntity.getAlbumName() != null) existingAlbum.setAlbumName(albumEntity.getAlbumName());
            if (albumEntity.getMusicList() != null && !albumEntity.getMusicList().isEmpty()) existingAlbum.setMusicList(albumEntity.getMusicList());
            return existingAlbum;
        }).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "album with id %d was not found".formatted(albumId)));
    }

    public void deleteAlbumIdById(Long albumId) {
        albumRepository.deleteById(albumId);
    }
}
