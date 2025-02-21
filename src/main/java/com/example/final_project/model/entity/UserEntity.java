package com.example.final_project.model.entity;

import com.example.final_project.model.enums.UserRole;
import com.example.final_project.model.enums.UserStatus;
import com.example.final_project.model.request.UserRequest;
import jakarta.persistence.*;
import lombok.*;

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
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private UserStatus status;

    public static UserEntity toUserEntity(UserRequest userRequest){
        return UserEntity.builder()
                .username(userRequest.getUserName())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .role(userRequest.getRole())
                .status(userRequest.getStatus())
                .build();
    }
}
