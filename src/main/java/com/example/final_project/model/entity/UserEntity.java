package com.example.final_project.model.entity;

import com.example.final_project.component.Utils;
import com.example.final_project.model.enums.UserRole;
import com.example.final_project.model.enums.UserStatus;
import com.example.final_project.model.request.UserRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private Long userId;

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

    @Column(name = "create_date", nullable = false)
    public LocalDateTime created;

    @Column(name = "modify_date")
    public LocalDateTime modified;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "ver_code_exp_date")
    private LocalDateTime verificationCodeExpDate;

    @OneToMany(mappedBy = "author")
    private List<MusicEntity> musicList = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<PlaylistEntity> playlists = new ArrayList<>();

    public static UserEntity toUserEntity(UserRequest userRequest) {
        return UserEntity.builder()
                .username(userRequest.getUserName())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .userRole(userRequest.getRole())
                .build();
    }
}
