package com.example.final_project;

import com.example.final_project.component.Utils;
import com.example.final_project.exception.CustomException;
import com.example.final_project.facade.AuthFacade;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.enums.UserRole;
import com.example.final_project.model.enums.UserStatus;
import com.example.final_project.model.response.UserResponse;
import com.example.final_project.security.CustomAuthentication;
import com.example.final_project.service.MailSenderService;
import com.example.final_project.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthFacadeTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserService userService;
    @Mock
    private MailSenderService mailSenderService;

    @InjectMocks
    private AuthFacade authFacade;

    private UserEntity mockUser(UserStatus status) {
        return UserEntity.builder()
                .id(1L)
                .username("testUser")
                .email("user@example.com")
                .password("encodedPassword")
                .userRole(UserRole.LISTENER)
                .userStatus(status)
                .build();
    }

    @Test
    void signIn_shouldReturnToken() {
        UserEntity user = mockUser(UserStatus.ACTIVE);

        when(userService.findFirstByUsernameEquals("testUser")).thenReturn(user);
        when(passwordEncoder.matches("123456", "encodedPassword")).thenReturn(true);

        String token = authFacade.signIn("testUser", "123456");

        assertNotNull(token);

        Authentication authentication = authFacade.authenticate(token);

        assertNotNull(authentication);
        assertEquals(1L, authentication.getPrincipal());
        assertEquals("testUser", authentication.getName());
        assertEquals(List.of(new SimpleGrantedAuthority("ROLE_LISTENER")), authentication.getAuthorities());
    }

    @Test
    void signIn_shouldThrowExceptionWhenUserPending() {
        UserEntity user = mockUser(UserStatus.PENDING);

        when(userService.findFirstByUsernameEquals("testUser")).thenReturn(user);

        CustomException exception = assertThrows(CustomException.class, () ->
                authFacade.signIn("testUser", "123456")
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("User is not activated", exception.getMessage());
    }

    @Test
    void signIn_shouldThrowExceptionWhenUserBlocked() {
        UserEntity user = mockUser(UserStatus.BLOCKED);

        when(userService.findFirstByUsernameEquals("testUser")).thenReturn(user);

        CustomException exception = assertThrows(CustomException.class, () ->
                authFacade.signIn("testUser", "123456")
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("User is blocked! contact admin", exception.getMessage());
    }

    @Test
    void signIn_shouldThrowExceptionWhenPasswordIncorrect() {
        UserEntity user = mockUser(UserStatus.ACTIVE);

        when(userService.findFirstByUsernameEquals("testUser")).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () ->
                authFacade.signIn("testUser", "wrongPassword")
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("UserName or Password is incorrect!", exception.getMessage());
    }

    @Test
    void signUp_shouldRegisterUser() {
        UserEntity user = mockUser(UserStatus.PENDING);
        user.setPassword("123456");

        try (MockedStatic<Utils> utilities = mockStatic(Utils.class)) {
            utilities.when(Utils::generateVerificationCode).thenReturn("123456");

            when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
            when(userService.save(any())).thenReturn(user);

            UserResponse response = authFacade.signUp(user);

            assertEquals(user.getUsername(), response.getUsername());
            verify(mailSenderService).sendVerificationEmail(eq(user.getEmail()), any(), contains("123456"));
        }
    }

    @Test
    void verifyUser_shouldActivateUser() {
        UserEntity user = mockUser(UserStatus.PENDING);
        user.setVerificationCode("123456");
        user.setVerificationCodeExpDate(LocalDateTime.now().plusMinutes(10));

        when(userService.findUserByEmail("user@example.com")).thenReturn(user);

        String result = authFacade.verifyUser("user@example.com", "123456");

        assertEquals("user testUser verified", result);
        assertEquals(UserStatus.ACTIVE, user.getUserStatus());
        assertNull(user.getVerificationCode());
        verify(userService).save(user);
    }

    @Test
    void verifyUser_shouldThrowExceptionForWrongCode() {
        UserEntity user = mockUser(UserStatus.PENDING);
        user.setVerificationCode("123456");

        when(userService.findUserByEmail("user@example.com")).thenReturn(user);

        CustomException exception = assertThrows(CustomException.class, () ->
                authFacade.verifyUser("user@example.com", "wrongCode")
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("Verification code is incorrect!", exception.getMessage());
    }

    @Test
    void resendVerificationCode_shouldSendNewCode() {
        UserEntity user = mockUser(UserStatus.PENDING);

        try (MockedStatic<Utils> utilities = mockStatic(Utils.class)) {
            utilities.when(Utils::generateVerificationCode).thenReturn("654321");

            when(userService.findUserByEmail("user@example.com")).thenReturn(user);
            when(userService.save(user)).thenReturn(user);

            authFacade.resendVerificationCode("user@example.com");

            assertEquals("654321", user.getVerificationCode());
            verify(mailSenderService).sendVerificationEmail(eq(user.getEmail()), any(), contains("654321"));
        }
    }

    @Test
    void resendVerificationCode_shouldThrowExceptionIfUserActive() {
        UserEntity user = mockUser(UserStatus.ACTIVE);

        when(userService.findUserByEmail("user@example.com")).thenReturn(user);

        CustomException exception = assertThrows(CustomException.class, () ->
                authFacade.resendVerificationCode("user@example.com")
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("User is already active.", exception.getMessage());
    }
}
