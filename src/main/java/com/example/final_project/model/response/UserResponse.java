package com.example.final_project.model.response;

import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.enums.UserRole;
import com.example.final_project.model.enums.UserStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private UserRole role;
    private UserStatus status;
    private LocalDateTime created;
    private LocalDateTime modified;

    public static UserResponse toUserResponse(UserEntity userEntity){
        return UserResponse.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .role(userEntity.getUserRole())
                .status(userEntity.getUserStatus())
                .created(userEntity.getCreated())
                .modified(userEntity.getModified())
                .build();
    }
}
