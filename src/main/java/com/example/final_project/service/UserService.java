package com.example.final_project.service;

import com.example.final_project.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.enums.UserStatus;
import com.example.final_project.repository.UserRepository;
import com.example.final_project.security.CustomAuthentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private static final String secret = UUID.randomUUID().toString();

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public String logIn(String username, String password) {
        UserEntity userEntity = userRepository.findFirstByUsernameEquals(username)
                .orElseThrow(() -> new CustomException(HttpStatus.FORBIDDEN, "UserName or Password is incorrect!"));
        if (!passwordEncoder.matches(password, userEntity.getPassword()))
            throw new CustomException(HttpStatus.FORBIDDEN, "UserName or Password is incorrect!");

        return Jwts.builder()
                .claim("username", userEntity.getUsername())
                .claim("id", userEntity.getId())
                .claim("role", userEntity.getRole().toString())
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

    public UserEntity registerUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setUserStatus(UserStatus.PENDING);
        return userRepository.save(userEntity);
    }

    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "User with id %d was not found".formatted(userId)));
    }

    @Transactional
    public UserEntity changeUserStatus(Long userId, UserStatus userStatus) {
        UserEntity userEntity = getUserById(userId);
        userEntity.setUserStatus(userStatus);
        return userEntity;
    }

    public Page<UserEntity> getAllUsers(Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return userRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy)));
    }
}