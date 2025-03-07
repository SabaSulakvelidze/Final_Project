package com.example.final_project;

import com.example.final_project.component.Utils;
import com.example.final_project.exception.CustomException;
import com.example.final_project.facade.AlbumFacade;
import com.example.final_project.model.entity.AlbumEntity;
import com.example.final_project.model.entity.MusicEntity;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.enums.UserRole;
import com.example.final_project.model.enums.UserStatus;
import com.example.final_project.model.request.AlbumRequest;
import com.example.final_project.model.response.AlbumResponse;
import com.example.final_project.service.AlbumService;
import com.example.final_project.service.MusicService;
import com.example.final_project.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumFacadeTest {

    @Mock
    private AlbumService albumService;
    @Mock
    private MusicService musicService;
    @Mock
    private UserService userService;

    @InjectMocks
    private AlbumFacade albumFacade;

    private UserEntity mockUser() {
        return UserEntity.builder()
                .id(1L)
                .username("testUser")
                .email("user@example.com")
                .userRole(UserRole.LISTENER)
                .userStatus(UserStatus.ACTIVE)
                .build();
    }

    private AlbumEntity mockAlbum(UserEntity owner) {
        return AlbumEntity.builder()
                .id(1L)
                .albumName("Test Album")
                .owner(owner)
                .build();
    }

    private MusicEntity mockMusic() {
        return MusicEntity.builder()
                .id(2L)
                .musicName("Test Music")
                .build();
    }

    @Test
    void addAlbum_shouldReturnAlbumResponse() {
        try (MockedStatic<Utils> utilities = mockStatic(Utils.class)) {
            utilities.when(Utils::getCurrentUserId).thenReturn(1L);

            UserEntity currentUser = mockUser();
            AlbumRequest request = new AlbumRequest();
            request.setMusicIdList(Set.of(2L));
            AlbumEntity albumEntity = mockAlbum(currentUser);
            MusicEntity musicEntity = mockMusic();

            when(userService.findUserById(1L)).thenReturn(currentUser);
            when(albumService.save(any())).thenReturn(albumEntity);
            when(musicService.findMusicById(2L)).thenReturn(musicEntity);
            when(musicService.save(musicEntity)).thenReturn(musicEntity);

            AlbumResponse response = albumFacade.addAlbum(request);

            assertEquals("Test Album", response.getAlbumName());
            assertEquals(1L, response.getAlbumId());
            verify(musicService).save(musicEntity);
        }
    }

    @Test
    void findAlbumById_shouldReturnAlbumResponse() {
        AlbumEntity albumEntity = mockAlbum(mockUser());
        when(albumService.findAlbumById(1L)).thenReturn(albumEntity);

        AlbumResponse response = albumFacade.findAlbumById(1L);

        assertEquals("Test Album", response.getAlbumName());
    }

    @Test
    void getAlbums_shouldReturnAlbumPage() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "albumName"));
        Page<AlbumEntity> albumPage = new PageImpl<>(Collections.emptyList());

        when(albumService.getAlbums(any(), eq(pageRequest))).thenReturn(albumPage);

        Page<AlbumResponse> response = albumFacade.getAlbums(null, 0, 10, Sort.Direction.ASC, "albumName");

        assertEquals(0, response.getTotalElements());
    }

    @Test
    void updateAlbumById_shouldUpdateAndReturnAlbumResponse() {
        try (MockedStatic<Utils> utilities = mockStatic(Utils.class)) {
            utilities.when(() -> Utils.checkIfCurrentUserIsOwner(anyString())).thenAnswer(invocation -> null);

            Long albumId = 1L;
            AlbumRequest request = new AlbumRequest();
            request.setMusicIdList(Set.of(2L));
            AlbumEntity existingAlbum = mockAlbum(mockUser());
            AlbumEntity updatedAlbum = mockAlbum(existingAlbum.getOwner());
            MusicEntity musicEntity = mockMusic();

            when(albumService.findAlbumById(albumId)).thenReturn(existingAlbum);
            when(albumService.updateAlbumById(eq(albumId), any())).thenReturn(updatedAlbum);
            when(musicService.findMusicById(2L)).thenReturn(musicEntity);
            when(musicService.save(musicEntity)).thenReturn(musicEntity);

            AlbumResponse response = albumFacade.updateAlbumById(albumId, request);

            assertEquals("Test Album", response.getAlbumName());
        }
    }

    @Test
    void deleteAlbumById_shouldDeleteAlbum() {
        try (MockedStatic<Utils> utilities = mockStatic(Utils.class)) {
            utilities.when(() -> Utils.checkIfCurrentUserIsOwner(anyString())).thenAnswer(invocation -> null);

            AlbumEntity albumEntity = mockAlbum(mockUser());
            when(albumService.findAlbumById(1L)).thenReturn(albumEntity);

            albumFacade.deleteAlbumById(1L);

            verify(albumService).deleteAlbumIdById(1L);
        }
    }

    @Test
    void addMusicToAlbum_shouldThrowExceptionWhenMusicAlreadyInAlbum() {
        try (MockedStatic<Utils> utilities = mockStatic(Utils.class)) {
            utilities.when(Utils::getCurrentUserId).thenReturn(1L);

            UserEntity currentUser = mockUser();
            AlbumRequest request = new AlbumRequest();
            request.setMusicIdList(Set.of(2L));
            AlbumEntity albumEntity = mockAlbum(currentUser);
            MusicEntity musicEntity = mockMusic();
            musicEntity.setAlbum(mockAlbum(currentUser)); // Music already in an album

            when(userService.findUserById(1L)).thenReturn(currentUser);
            when(albumService.save(any())).thenReturn(albumEntity);
            when(musicService.findMusicById(2L)).thenReturn(musicEntity);

            CustomException exception = assertThrows(CustomException.class, () ->
                    albumFacade.addAlbum(request)
            );

            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertTrue(exception.getMessage().contains("music with id 2 exist in album"));
        }
    }
}
