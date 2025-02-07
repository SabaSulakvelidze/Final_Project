package org.example.final_project;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.example.final_project.exception.InvalidUserException;
import org.example.final_project.exception.ResourceNotFoundException;
import org.example.final_project.model.entity.UserEntity;
import org.example.final_project.model.enums.Role;
import org.example.final_project.model.enums.UserStatus;
import org.example.final_project.repository.UserRepository;
import org.example.final_project.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.*;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);
        user.setUserName("testuser");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);
    }

    @Test
    void logInShouldReturnJwtTokenWhenCredentialsAreValid() {
        when(userRepository.findFirstByUserNameEquals("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        String token = userService.logIn("testuser", "password");
        assertNotNull(token);
        assertTrue(token.length() > 10);
    }

    @Test
    void logInShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findFirstByUserNameEquals("testuser")).thenReturn(Optional.empty());

        assertThrows(InvalidUserException.class, () -> userService.logIn("testuser", "password"));
    }

    @Test
    void logInShouldThrowExceptionWhenPasswordIsIncorrect() {
        when(userRepository.findFirstByUserNameEquals("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        assertThrows(InvalidUserException.class, () -> userService.logIn("testuser", "wrongpassword"));
    }

    @Test
    void registerUserShouldEncodePasswordAndSaveUser() {
        user.setPassword("rawPassword");
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity savedUser = userService.registerUser(user);
        assertEquals("encodedPassword", savedUser.getPassword());
    }

    @Test
    void getUserByIdShouldReturnUserWhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserEntity foundUser = userService.getUserById(1L);
        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
    }

    @Test
    void getUserByIdShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void changeUserStatusShouldUpdateUserStatus() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity updatedUser = userService.changeUserStatus(1L, UserStatus.INACTIVE);
        assertEquals(UserStatus.INACTIVE, updatedUser.getUserStatus());
    }

    @Test
    void getAllUsersShouldReturnPageOfUsers() {
        Page<UserEntity> userPage = new PageImpl<>(List.of(user));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        Page<UserEntity> result = userService.getAllUsers(0, 5, Sort.Direction.ASC, "id");
        assertEquals(1, result.getContent().size());
    }
}

