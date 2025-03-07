package com.example.final_project;

import com.example.final_project.exception.CustomException;
import com.example.final_project.model.entity.MusicEntity;
import com.example.final_project.repository.MusicRepository;
import com.example.final_project.service.MusicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MusicServiceTest {

    @Mock
    private MusicRepository musicRepository;

    @InjectMocks
    private MusicService musicService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        MusicEntity musicEntity = new MusicEntity();
        when(musicRepository.save(musicEntity)).thenReturn(musicEntity);
        MusicEntity result = musicService.save(musicEntity);
        assertNotNull(result);
        verify(musicRepository, times(1)).save(musicEntity);
    }

    @Test
    void testGetMusicEntities() {
        Specification<MusicEntity> specification = mock(Specification.class);
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<MusicEntity> musicList = new ArrayList<>();
        Page<MusicEntity> musicPage = new PageImpl<>(musicList);
        when(musicRepository.findAll(specification, pageRequest)).thenReturn(musicPage);
        Page<MusicEntity> result = musicService.getMusicEntities(specification, pageRequest);
        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        verify(musicRepository, times(1)).findAll(specification, pageRequest);
    }

    @Test
    void testFindMusicById_Success() {
        Long musicId = 1L;
        MusicEntity musicEntity = new MusicEntity();
        musicEntity.setId(musicId);
        when(musicRepository.findById(musicId)).thenReturn(Optional.of(musicEntity));
        MusicEntity result = musicService.findMusicById(musicId);
        assertNotNull(result);
        assertEquals(musicId, result.getId());
        verify(musicRepository, times(1)).findById(musicId);
    }

    @Test
    void testFindMusicById_NotFound() {
        Long musicId = 1L;
        when(musicRepository.findById(musicId)).thenReturn(Optional.empty());
        CustomException thrown = assertThrows(CustomException.class, () -> musicService.findMusicById(musicId));
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        assertEquals("music with id %d was not found".formatted(musicId), thrown.getMessage());
        verify(musicRepository, times(1)).findById(musicId);
    }

    @Test
    void testUpdateMusicById_Success() {
        // Arrange
        Long musicId = 1L;
        MusicEntity existingMusic = new MusicEntity();
        existingMusic.setId(musicId);
        existingMusic.setMusicName("Old Name");
        MusicEntity updatedMusic = new MusicEntity();
        updatedMusic.setMusicName("New Name");
        when(musicRepository.findById(musicId)).thenReturn(Optional.of(existingMusic));
        MusicEntity result = musicService.updateMusicById(musicId, updatedMusic);
        assertNotNull(result);
        assertEquals("New Name", result.getMusicName());
        verify(musicRepository, times(1)).findById(musicId);
    }

    @Test
    void testUpdateMusicById_NotFound() {
        Long musicId = 1L;
        MusicEntity updatedMusic = new MusicEntity();
        when(musicRepository.findById(musicId)).thenReturn(Optional.empty());
        CustomException thrown = assertThrows(CustomException.class, () -> musicService.updateMusicById(musicId, updatedMusic));
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        assertEquals("Music with id %d was not found".formatted(musicId), thrown.getMessage());
        verify(musicRepository, times(1)).findById(musicId);
    }

    @Test
    void testDeleteMusicById() {
        Long musicId = 1L;
        doNothing().when(musicRepository).deleteById(musicId);
        musicService.deleteMusicById(musicId);
        verify(musicRepository, times(1)).deleteById(musicId);
    }
}