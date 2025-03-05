package com.example.final_project.service;

import com.example.final_project.exception.CustomException;
import com.example.final_project.model.entity.AlbumEntity;
import com.example.final_project.model.entity.MusicEntity;
import com.example.final_project.model.enums.MusicGenre;
import com.example.final_project.model.specification.AlbumSpecification;
import com.example.final_project.model.specification.MusicSpecification;
import com.example.final_project.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    public AlbumEntity findAlbumById(Long albumId) {
        return albumRepository.findById(albumId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "album with id %d was not found".formatted(albumId)));
    }

    public Page<AlbumEntity> getAlbums(Specification<AlbumEntity> specification, PageRequest pageRequest) {
        return albumRepository.findAll(specification, pageRequest);
    }

    @Transactional
    public AlbumEntity updateAlbumById(Long albumId, AlbumEntity albumEntity) {
        return albumRepository.findById(albumId).map(existingAlbum -> {
            if (albumEntity.getAlbumName() != null) existingAlbum.setAlbumName(albumEntity.getAlbumName());
            if (albumEntity.getMusicList() != null && !albumEntity.getMusicList().isEmpty())
                existingAlbum.setMusicList(albumEntity.getMusicList());
            return existingAlbum;
        }).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "album with id %d was not found".formatted(albumId)));
    }

    public void deleteAlbumIdById(Long albumId) {
        albumRepository.deleteById(albumId);
    }
}
