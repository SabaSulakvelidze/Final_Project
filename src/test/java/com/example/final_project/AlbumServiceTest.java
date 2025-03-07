package com.example.final_project;

import com.example.final_project.exception.CustomException;
import com.example.final_project.model.entity.AlbumEntity;
import com.example.final_project.repository.AlbumRepository;
import com.example.final_project.service.AlbumService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository;

    @InjectMocks
    private AlbumService albumService;

    private final AlbumEntity albumEntity = AlbumEntity.builder()
            .id(1L)
            .albumName("Test Album")
            .build();

    @Test
    void save_ShouldReturnSavedAlbum() {
        when(albumRepository.save(albumEntity)).thenReturn(albumEntity);

        AlbumEntity result = albumService.save(albumEntity);

        assertEquals(albumEntity, result);
        verify(albumRepository).save(albumEntity);
    }

    @Test
    void findAlbumById_ShouldReturnAlbum_WhenFound() {
        when(albumRepository.findById(1L)).thenReturn(Optional.of(albumEntity));

        AlbumEntity result = albumService.findAlbumById(1L);

        assertEquals(albumEntity, result);
        verify(albumRepository).findById(1L);
    }

    @Test
    void findAlbumById_ShouldThrowException_WhenNotFound() {
        when(albumRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> albumService.findAlbumById(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("album with id 1 was not found", exception.getMessage());
        verify(albumRepository).findById(1L);
    }

    @Test
    void getAlbums_ShouldReturnPageOfAlbums() {
        Specification<AlbumEntity> specification = mock(Specification.class);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<AlbumEntity> page = new PageImpl<>(List.of(albumEntity));

        when(albumRepository.findAll(specification, pageRequest)).thenReturn(page);

        Page<AlbumEntity> result = albumService.getAlbums(specification, pageRequest);

        assertEquals(1, result.getTotalElements());
        assertEquals(albumEntity, result.getContent().getFirst());
        verify(albumRepository).findAll(specification, pageRequest);
    }

    @Test
    void updateAlbumById_ShouldUpdateAndReturnAlbum_WhenFound() {
        AlbumEntity updateInfo = new AlbumEntity();
        updateInfo.setAlbumName("Updated Album");

        when(albumRepository.findById(1L)).thenReturn(Optional.of(albumEntity));

        AlbumEntity result = albumService.updateAlbumById(1L, updateInfo);

        assertEquals("Updated Album", result.getAlbumName());
        verify(albumRepository).findById(1L);
    }

    @Test
    void updateAlbumById_ShouldThrowException_WhenNotFound() {
        AlbumEntity updateInfo = new AlbumEntity();
        updateInfo.setAlbumName("Updated Album");

        when(albumRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> albumService.updateAlbumById(1L, updateInfo));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("album with id 1 was not found", exception.getMessage());
        verify(albumRepository).findById(1L);
    }

    @Test
    void deleteAlbumIdById_ShouldDeleteAlbum() {
        albumService.deleteAlbumIdById(1L);

        verify(albumRepository).deleteById(1L);
    }
}
