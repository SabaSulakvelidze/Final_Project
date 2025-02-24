package com.example.final_project.facade;

import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public UserEntity findUserById(Long userId) {
        return userService.findUserById(userId);
    }

    public Page<UserEntity> findAllUsers(Integer pageNumber, Integer pageSize, Sort.Direction direction, String sortBy) {
        return userService.findAllUsers(pageNumber, pageSize, direction, sortBy);
    }

    public UserEntity updateUserById(Long userId, UserEntity updatedUser) {
        return userService.updateUserById(userId, updatedUser);
    }

    public void deleteUserById(Long userId) {
        userService.deleteUserById(userId);
    }
}
