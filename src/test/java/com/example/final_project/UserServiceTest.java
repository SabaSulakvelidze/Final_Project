package com.example.final_project;

import com.example.final_project.exception.CustomException;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.repository.UserRepository;
import com.example.final_project.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUser_Success() {
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        when(userRepository.save(user)).thenReturn(user);

        UserEntity result = userService.save(user);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void findFirstByUsernameEquals_UserExists() {
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        when(userRepository.findFirstByUsernameEquals("testUser")).thenReturn(Optional.of(user));

        UserEntity result = userService.findFirstByUsernameEquals("testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository, times(1)).findFirstByUsernameEquals("testUser");
    }

    @Test
    void findFirstByUsernameEquals_UserNotFound() {
        when(userRepository.findFirstByUsernameEquals("testUser")).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () ->
                userService.findFirstByUsernameEquals("testUser"));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("UserName or Password is incorrect!", exception.getMessage());
    }

    @Test
    void findUserById_UserExists() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserEntity result = userService.findUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () ->
                userService.findUserById(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("User with id 1 was not found", exception.getMessage());
    }

    @Test
    void findUserByEmail_UserExists() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        when(userRepository.findFirstByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserEntity result = userService.findUserByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findFirstByEmail("test@example.com");
    }

    @Test
    void findUserByEmail_UserNotFound() {
        when(userRepository.findFirstByEmail("test@example.com")).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () ->
                userService.findUserByEmail("test@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("User with email test@example.com was not found", exception.getMessage());
    }

    @Test
    void getUsers_Success() {
        Specification<UserEntity> spec = mock(Specification.class);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<UserEntity> page = mock(Page.class);

        when(userRepository.findAll(spec, pageRequest)).thenReturn(page);

        Page<UserEntity> result = userService.getUsers(spec, pageRequest);

        assertNotNull(result);
        verify(userRepository, times(1)).findAll(spec, pageRequest);
    }

    @Test
    void getAllUsers_Success() {
        UserEntity user1 = new UserEntity();
        UserEntity user2 = new UserEntity();
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserEntity> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void updateUserById_UserExists() {
        UserEntity existingUser = new UserEntity();
        existingUser.setId(1L);
        existingUser.setUsername("oldUsername");

        UserEntity updatedUser = new UserEntity();
        updatedUser.setUsername("newUsername");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        UserEntity result = userService.updateUserById(1L, updatedUser);

        assertNotNull(result);
        assertEquals("newUsername", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void updateUserById_UserNotFound() {
        UserEntity updatedUser = new UserEntity();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () ->
                userService.updateUserById(1L, updatedUser));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("User with id 1 was not found", exception.getMessage());
    }

    @Test
    void deleteUserById_Success() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}