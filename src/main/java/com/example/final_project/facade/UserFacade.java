package com.example.final_project.facade;

import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.enums.UserRole;
import com.example.final_project.model.enums.UserStatus;
import com.example.final_project.model.request.UserRequest;
import com.example.final_project.model.response.UserResponse;
import com.example.final_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public UserResponse findUserById(Long userId) {
        return UserResponse.toUserResponse(userService.findUserById(userId));
    }

    public Page<UserResponse> getUsers(String username, String email, UserRole userRole, UserStatus userStatus,
                                     Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return userService.getUsers(username, email, userRole, userStatus,
                                    pageNumber, pageSize, direction, sortBy).map(UserResponse::toUserResponse);
    }

    public UserResponse updateUserById(Long userId, UserRequest userRequest) {
        return UserResponse.toUserResponse(userService.updateUserById(userId, UserEntity.toUserEntity(userRequest)));
    }

    public void deleteUserById(Long userId) {
        userService.deleteUserById(userId);
    }
}
