package com.example.final_project;

import com.example.final_project.component.Utils;
import com.example.final_project.exception.CustomException;
import com.example.final_project.facade.MusicFacade;
import com.example.final_project.model.entity.MusicEntity;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.enums.MusicGenre;
import com.example.final_project.model.enums.UserRole;
import com.example.final_project.model.enums.UserStatus;
import com.example.final_project.model.request.MusicRequest;
import com.example.final_project.model.response.MusicResponse;
import com.example.final_project.service.AlbumService;
import com.example.final_project.service.MusicService;
import com.example.final_project.service.StatisticsService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MusicFacadeTest {

    @Mock
    private MusicService musicService;
    @Mock
    private UserService userService;
    @Mock
    private AlbumService albumService;
    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private MusicFacade musicFacade;

    private UserEntity mockUser() {
        return UserEntity.builder()
                .id(1L)
                .username("testUser")
                .email("user@example.com")
                .password("password")
                .userRole(UserRole.LISTENER)
                .userStatus(UserStatus.ACTIVE)
                .build();
    }

    private MusicEntity mockMusic(UserEntity author) {
        return MusicEntity.builder()
                .id(10L)
                .musicName("Test Song")
                .musicGenre(MusicGenre.ROCK)
                .author(author)
                .build();
    }

    @Test
    void addMusicEntity_shouldReturnMusicResponse() {
        try (MockedStatic<Utils> utilities = mockStatic(Utils.class)) {
            utilities.when(Utils::getCurrentUserId).thenReturn(1L);

            UserEntity currentUser = mockUser();
            MusicRequest request = new MusicRequest();
            MusicEntity savedMusic = mockMusic(currentUser);

            when(userService.findUserById(1L)).thenReturn(currentUser);
            when(musicService.save(any())).thenReturn(savedMusic);

            MusicResponse response = musicFacade.addMusicEntity(request);

            assertEquals("Test Song", response.getMusicName());
            assertEquals(MusicGenre.ROCK, response.getMusicGenre());
            verify(musicService).save(any(MusicEntity.class));
        }
    }

    @Test
    void getMusicEntities_shouldReturnMusicPage() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "musicName"));
        Page<MusicEntity> musicPage = new PageImpl<>(Collections.emptyList());

        when(musicService.getMusicEntities(any(), eq(pageRequest))).thenReturn(musicPage);

        Page<MusicResponse> response = musicFacade.getMusicEntities(null, null, null, null, 0, 10, Sort.Direction.ASC, "musicName");

        assertEquals(0, response.getTotalElements());
        verify(musicService).getMusicEntities(any(), eq(pageRequest));
    }

    @Test
    void findMusicById_shouldReturnMusicResponse() {
        try (MockedStatic<Utils> utilities = mockStatic(Utils.class)) {
            utilities.when(Utils::getCurrentUserId).thenReturn(1L);

            UserEntity currentUser = mockUser();
            MusicEntity music = mockMusic(currentUser);

            when(userService.findUserById(1L)).thenReturn(currentUser);
            when(musicService.findMusicById(10L)).thenReturn(music);

            MusicResponse response = musicFacade.findMusicById(10L);

            assertEquals("Test Song", response.getMusicName());
            assertEquals(MusicGenre.ROCK, response.getMusicGenre());
            verify(statisticsService).increasePlayCount(any());
        }
    }

    @Test
    void updateMusicById_shouldReturnUpdatedMusicResponse() {
        try (MockedStatic<Utils> utilities = mockStatic(Utils.class)) {
            utilities.when(() -> Utils.checkIfCurrentUserIsOwner(anyString())).thenAnswer(invocation -> null);

            Long musicId = 10L;
            MusicRequest request = new MusicRequest();
            UserEntity author = mockUser();
            MusicEntity existingMusic = mockMusic(author);
            MusicEntity updatedMusic = mockMusic(author);

            when(musicService.findMusicById(musicId)).thenReturn(existingMusic);
            when(musicService.updateMusicById(eq(musicId), any())).thenReturn(updatedMusic);

            MusicResponse response = musicFacade.updateMusicById(musicId, request);

            assertEquals("Test Song", response.getMusicName());
            assertEquals(MusicGenre.ROCK, response.getMusicGenre());
        }
    }

    @Test
    void updateMusicById_shouldThrowExceptionWhenNotOwner() {
        try (MockedStatic<Utils> utilities = mockStatic(Utils.class)) {
            utilities.when(() -> Utils.checkIfCurrentUserIsOwner(anyString()))
                    .thenThrow(new CustomException(HttpStatus.FORBIDDEN, "Not the owner"));

            Long musicId = 10L;
            MusicRequest request = new MusicRequest();
            UserEntity author = mockUser();
            MusicEntity existingMusic = mockMusic(author);

            when(musicService.findMusicById(musicId)).thenReturn(existingMusic);

            CustomException exception = assertThrows(CustomException.class, () -> musicFacade.updateMusicById(musicId, request));

            assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
            assertEquals("Not the owner", exception.getMessage());
        }
    }

    @Test
    void deleteMusicById_shouldCallDelete() {
        try (MockedStatic<Utils> utilities = mockStatic(Utils.class)) {
            utilities.when(() -> Utils.checkIfCurrentUserIsOwner(anyString())).thenAnswer(invocation -> null);

            Long musicId = 10L;
            UserEntity author = mockUser();
            MusicEntity music = mockMusic(author);

            when(musicService.findMusicById(musicId)).thenReturn(music);

            musicFacade.deleteMusicById(musicId);

            verify(musicService).deleteMusicById(musicId);
        }
    }

    @Test
    void deleteMusicById_shouldThrowExceptionWhenNotOwner() {
        try (MockedStatic<Utils> utilities = mockStatic(Utils.class)) {
            utilities.when(() -> Utils.checkIfCurrentUserIsOwner(anyString()))
                    .thenThrow(new CustomException(HttpStatus.FORBIDDEN, "Not the owner"));

            Long musicId = 10L;
            UserEntity author = mockUser();
            MusicEntity music = mockMusic(author);

            when(musicService.findMusicById(musicId)).thenReturn(music);

            CustomException exception = assertThrows(CustomException.class, () -> musicFacade.deleteMusicById(musicId));

            assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
            assertEquals("Not the owner", exception.getMessage());
        }
    }
}
