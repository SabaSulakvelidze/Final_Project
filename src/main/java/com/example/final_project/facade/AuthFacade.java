package com.example.final_project.facade;

import com.example.final_project.component.Utils;
import com.example.final_project.exception.CustomException;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.enums.UserStatus;
import com.example.final_project.security.CustomAuthentication;
import com.example.final_project.service.MailSenderService;
import com.example.final_project.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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

    public String logIn(String username, String password) {
        UserEntity userEntity = userService.findByUsername(username);
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

    public UserEntity signUp(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setUserStatus(UserStatus.PENDING);
        return userService.save(userEntity);
    }

    @Transactional
    public UserEntity activateUser(Long userId, String activationCode) {
        if(activationCode.length() != 6){

        }
        UserEntity userEntity = userService.findUserById(userId);
        userEntity.setUserStatus(UserStatus.ACTIVE);
        return userEntity;
    }
}
