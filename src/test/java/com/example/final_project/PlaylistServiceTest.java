package com.example.final_project;

import com.example.final_project.exception.CustomException;
import com.example.final_project.model.entity.PlaylistEntity;
import com.example.final_project.repository.PlaylistRepository;
import com.example.final_project.service.PlaylistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaylistServiceTest {

    private PlaylistService playlistService;

    @Mock
    private PlaylistRepository playlistRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        playlistService = new PlaylistService(playlistRepository);
    }

    @Test
    void save_ShouldSaveAndReturnPlaylistEntity() {
        PlaylistEntity playlistEntity = new PlaylistEntity();
        when(playlistRepository.save(playlistEntity)).thenReturn(playlistEntity);

        PlaylistEntity result = playlistService.save(playlistEntity);

        assertEquals(playlistEntity, result);
        verify(playlistRepository, times(1)).save(playlistEntity);
    }

    @Test
    void getPlaylists_ShouldReturnPageOfPlaylistEntities() {
        Specification<PlaylistEntity> spec = mock(Specification.class);
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<PlaylistEntity> playlistEntities = Collections.singletonList(new PlaylistEntity());
        Page<PlaylistEntity> page = new PageImpl<>(playlistEntities);
        when(playlistRepository.findAll(spec, pageRequest)).thenReturn(page);

        Page<PlaylistEntity> result = playlistService.getPlaylists(spec, pageRequest);

        assertEquals(page, result);
        verify(playlistRepository, times(1)).findAll(spec, pageRequest);
    }

    @Test
    void findPlaylistById_ValidId_ShouldReturnPlaylistEntity() {
        Long playlistId = 1L;
        PlaylistEntity playlistEntity = new PlaylistEntity();
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlistEntity));

        PlaylistEntity result = playlistService.findPlaylistById(playlistId);

        assertEquals(playlistEntity, result);
        verify(playlistRepository, times(1)).findById(playlistId);
    }

    @Test
    void findPlaylistById_InvalidId_ShouldThrowCustomException() {
        Long playlistId = 2L;
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> playlistService.findPlaylistById(playlistId));
        assertEquals("playlist with id 2 was not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(playlistRepository, times(1)).findById(playlistId);
    }

    @Test
    void updatePlaylistById_ShouldUpdateAndReturnPlaylistEntity() {
        Long playlistId = 3L;
        PlaylistEntity existingPlaylist = new PlaylistEntity();
        PlaylistEntity updatedData = new PlaylistEntity();
        updatedData.setPlaylistName("Updated Name");
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(existingPlaylist));

        PlaylistEntity result = playlistService.updatePlaylistById(playlistId, updatedData);

        assertEquals(existingPlaylist, result);
        assertEquals("Updated Name", result.getPlaylistName());
        verify(playlistRepository, times(1)).findById(playlistId);
    }

    @Test
    void updatePlaylistById_InvalidId_ShouldThrowCustomException() {
        Long playlistId = 4L;
        PlaylistEntity updatedData = new PlaylistEntity();
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> playlistService.updatePlaylistById(playlistId, updatedData));
        assertEquals("playlist with id 4 was not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(playlistRepository, times(1)).findById(playlistId);
    }

    @Test
    void deletePlaylistIdById_ShouldDeletePlaylist() {
        Long playlistId = 5L;
        doNothing().when(playlistRepository).deleteById(playlistId);

        playlistService.deletePlaylistIdById(playlistId);

        verify(playlistRepository, times(1)).deleteById(playlistId);
    }
}