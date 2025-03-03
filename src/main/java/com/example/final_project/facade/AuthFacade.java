package com.example.final_project.facade;

import com.example.final_project.component.Utils;
import com.example.final_project.exception.CustomException;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.enums.UserRole;
import com.example.final_project.model.enums.UserStatus;
import com.example.final_project.model.response.UserResponse;
import com.example.final_project.security.CustomAuthentication;
import com.example.final_project.service.MailSenderService;
import com.example.final_project.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthFacade {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final MailSenderService mailSenderService;
    private static final String secret = UUID.randomUUID().toString();

    @PostConstruct
    public void initAdminUser() {
        userService.save(UserEntity.builder()
                .username("admin")
                .password(passwordEncoder.encode("123456"))
                .email("demo.project006@gmail.com")
                .userRole(UserRole.ADMIN)
                .userStatus(UserStatus.ACTIVE)
                .build());
    }

    public String signIn(String username, String password) {
        UserEntity userEntity = userService.findFirstByUsernameEquals(username);
        if (userEntity.getUserStatus() == UserStatus.PENDING)
            throw new CustomException(HttpStatus.BAD_REQUEST, "User is not activated");
        else if (userEntity.getUserStatus() == UserStatus.BLOCKED)
            throw new CustomException(HttpStatus.FORBIDDEN, "User is blocked! contact admin");
        if (!passwordEncoder.matches(password, userEntity.getPassword()))
            throw new CustomException(HttpStatus.FORBIDDEN, "UserName or Password is incorrect!");

        return Jwts.builder()
                .claim("username", userEntity.getUsername())
                .claim("id", userEntity.getId())
                .claim("role", userEntity.getUserRole().toString())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(30, ChronoUnit.MINUTES)))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public Authentication authenticate(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token);

        Claims payload = claimsJws.getPayload();
        Long id = payload.get("id", Long.class);
        String username = payload.get("username", String.class);
        String role = payload.get("role", String.class);
        return new CustomAuthentication(id, role, username);
    }

    public UserResponse signUp(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setUserStatus(UserStatus.PENDING);
        userEntity.setVerificationCode(Utils.generateVerificationCode());
        userEntity.setVerificationCodeExpDate(LocalDateTime.now().plusMinutes(30));

        sendVerificationCode(userEntity);
        return UserResponse.toUserResponse(userService.save(userEntity));
    }

    public String verifyUser(String userEmail, String verificationCode) {
        UserEntity userEntity = userService.findUserByEmail(userEmail);
        if (userEntity.getUserStatus() == UserStatus.ACTIVE)
            throw new CustomException(HttpStatus.FORBIDDEN, "User is already active.");
        if (userEntity.getUserStatus() == UserStatus.BLOCKED)
            throw new CustomException(HttpStatus.FORBIDDEN, "User is blocked. Please contact administrator.");
        if (userEntity.getUserStatus() != UserStatus.PENDING)
            throw new CustomException(HttpStatus.FORBIDDEN, "Incorrect user status.");
        if (!verificationCode.equals(userEntity.getVerificationCode()))
            throw new CustomException(HttpStatus.FORBIDDEN, "Verification code is incorrect!");
        if (userEntity.getVerificationCodeExpDate().isBefore(LocalDateTime.now()))
            throw new CustomException(HttpStatus.FORBIDDEN, "Verification code expired!");

        userEntity.setVerificationCode(null);
        userEntity.setVerificationCodeExpDate(null);
        userEntity.setUserStatus(UserStatus.ACTIVE);
        userService.save(userEntity);
        return "user %s verified".formatted(userEntity.getUsername());
    }

    public void resendVerificationCode(String userEmail) {
        UserEntity userEntity = userService.findUserByEmail(userEmail);
        if (userEntity.getUserStatus() == UserStatus.ACTIVE)
            throw new CustomException(HttpStatus.FORBIDDEN, "User is already active.");
        userEntity.setVerificationCode(Utils.generateVerificationCode());
        userEntity.setVerificationCodeExpDate(LocalDateTime.now().plusMinutes(30));
        sendVerificationCode(userService.save(userEntity));
    }

    public void sendVerificationCode(UserEntity userEntity) {
        String subject = "Account Verification";
        String verificationCode = userEntity.getVerificationCode();
        String htmlMessage = """
                <html>
                <body style=\\"font-family: Arial, sans-serif;\\">
                <div style=\\"background-color: #f5f5f5; padding: 20px;\\">
                <h2 style=\\"color: #333;\\">Welcome to our app!</h2>
                <p style=\\"font-size: 16px;\\">Please enter the verification code below to continue:</p>
                <div style=\\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\\">
                <h3 style=\\"color: #333;\\">Verification Code:</h3>
                <p style=\\"font-size: 18px; font-weight: bold; color: #007bff;\\">" %s "</p>
                </div>
                </div>
                </body>
                </html>
                """.formatted(verificationCode);
        mailSenderService.sendVerificationEmail(userEntity.getEmail(), subject, htmlMessage);

    }


}
