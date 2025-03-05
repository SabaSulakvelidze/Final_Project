package com.example.final_project.model.entity;

import com.example.final_project.model.enums.UserRole;
import com.example.final_project.model.enums.UserStatus;
import com.example.final_project.model.request.UserRequest;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "user_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "ver_code_exp_date")
    private LocalDateTime verificationCodeExpDate;

    @OneToMany(mappedBy = "author")
    @Builder.Default
    private Set<MusicEntity> musicList = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    @Builder.Default
    private Set<AlbumEntity> albumList = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    @Builder.Default
    private Set<PlaylistEntity> playlists = new HashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StatisticsEntity> stats;

    public static UserEntity toUserEntity(UserRequest userRequest) {
        return UserEntity.builder()
                .username(userRequest.getUserName())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .userRole(userRequest.getUserRole())
                .userStatus(userRequest.getUserStatus())
                .build();
    }
}
