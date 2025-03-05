package com.example.final_project.service;

import com.example.final_project.exception.CustomException;
import com.example.final_project.model.entity.MusicEntity;
import com.example.final_project.model.enums.MusicGenre;
import com.example.final_project.model.specification.MusicSpecification;
import com.example.final_project.repository.MusicRepository;
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
public class MusicService {
    private final MusicRepository musicRepository;

    public MusicEntity save(MusicEntity musicEntity) {
        return musicRepository.save(musicEntity);
    }

    public Page<MusicEntity> getMusicEntities(Specification<MusicEntity> specification, PageRequest pageRequest) {
        return musicRepository.findAll(specification,pageRequest);
    }

    public MusicEntity findMusicById(Long musicId) {
        return musicRepository.findById(musicId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "music with id %d was not found".formatted(musicId)));
    }

    @Transactional
    public MusicEntity updateMusicById(Long musicId, MusicEntity updatedMusic) {
        return musicRepository.findById(musicId).map(existingMusic -> {
            if (updatedMusic.getMusicName() != null) existingMusic.setMusicName(updatedMusic.getMusicName());
            if (updatedMusic.getMusicGenre() != null) existingMusic.setMusicGenre(updatedMusic.getMusicGenre());
            if (updatedMusic.getAuthor() != null) existingMusic.setAuthor(updatedMusic.getAuthor());
            if (updatedMusic.getAlbum() != null) existingMusic.setAlbum(updatedMusic.getAlbum());
            return existingMusic;
        }).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Music with id %d was not found".formatted(musicId)));
    }

    public void deleteMusicById(Long musicId) {
        musicRepository.deleteById(musicId);
    }

}
