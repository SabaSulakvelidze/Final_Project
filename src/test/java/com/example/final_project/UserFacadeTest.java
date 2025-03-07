package com.example.final_project;

import com.example.final_project.facade.UserFacade;
import com.example.final_project.model.entity.MusicEntity;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.enums.MusicGenre;
import com.example.final_project.model.enums.UserRole;
import com.example.final_project.model.enums.UserStatus;
import com.example.final_project.model.request.UserRequest;
import com.example.final_project.model.response.UserResponse;
import com.example.final_project.service.MusicService;
import com.example.final_project.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {

    @Mock
    private UserService userService;
    @Mock
    private MusicService musicService;

    @InjectMocks
    private UserFacade userFacade;

    private UserEntity mockUser(UserRole userRole) {
        return UserEntity.builder()
                .id(1L)
                .username("artistUser")
                .email("artist@example.com")
                .userRole(userRole)
                .userStatus(UserStatus.ACTIVE)
                .build();
    }

    private MusicEntity mockMusic(UserEntity author) {
        return MusicEntity.builder()
                .id(2L)
                .musicName("Test Song")
                .musicGenre(MusicGenre.ROCK)
                .author(author)
                .build();
    }

    @Test
    void findUserById_shouldReturnArtistWithSimilarArtists() {
        UserEntity artistUser = mockUser(UserRole.ARTIST);

        MusicEntity artistMusic = mockMusic(artistUser);

        UserEntity similarArtist = UserEntity.builder()
                .id(2L)
                .username("similarArtist")
                .build();

        MusicEntity similarMusic = mockMusic(similarArtist);

        when(userService.findUserById(1L)).thenReturn(artistUser);
        when(musicService.getMusicEntities(any(), any()))
                .thenReturn(new PageImpl<>(List.of(artistMusic)))
                .thenReturn(new PageImpl<>(List.of(similarMusic)));

        UserResponse response = userFacade.findUserById(1L);

        assertEquals("artistUser", response.getUsername());
        assertInstanceOf(UserResponse.class, response);
    }

    @Test
    void findUserById_shouldReturnRegularUserResponse() {
        UserEntity regularUser = mockUser(UserRole.LISTENER);

        when(userService.findUserById(1L)).thenReturn(regularUser);

        UserResponse response = userFacade.findUserById(1L);

        assertEquals("artistUser", response.getUsername());
        assertInstanceOf(UserResponse.class, response);
    }

    @Test
    void getUsers_shouldReturnPagedUsers() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "username"));
        Page<UserEntity> userPage = new PageImpl<>(Collections.emptyList());

        when(userService.getUsers(any(), eq(pageRequest))).thenReturn(userPage);

        Page<UserResponse> result = userFacade.getUsers(
                "username", "email@example.com", UserRole.ARTIST, UserStatus.ACTIVE,
                0, 10, Sort.Direction.ASC, "username"
        );

        assertEquals(0, result.getTotalElements());
        verify(userService).getUsers(any(), eq(pageRequest));
    }

    @Test
    void updateUserById_shouldUpdateUser() {
        UserRequest userRequest = new UserRequest();
        UserEntity updatedUser = mockUser(UserRole.LISTENER);

        when(userService.updateUserById(eq(1L), any())).thenReturn(updatedUser);

        UserResponse response = userFacade.updateUserById(1L, userRequest);

        assertEquals("artistUser", response.getUsername());
        verify(userService).updateUserById(eq(1L), any());
    }

    @Test
    void deleteUserById_shouldDeleteUser() {
        userFacade.deleteUserById(1L);

        verify(userService).deleteUserById(1L);
    }
}
