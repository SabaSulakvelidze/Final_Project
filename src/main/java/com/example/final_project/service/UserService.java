package com.example.final_project.service;

import com.example.final_project.exception.CustomException;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public UserEntity findFirstByUsernameEquals(String username) {
        return userRepository.findFirstByUsernameEquals(username)
                .orElseThrow(() -> new CustomException(HttpStatus.FORBIDDEN, "UserName or Password is incorrect!"));
    }

    public UserEntity findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "User with id %d was not found".formatted(userId)));
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findFirstByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "User with email %s was not found".formatted(email)));
    }

    public Page<UserEntity> getUsers(Specification<UserEntity> specification, PageRequest pageRequest) {
        return userRepository.findAll(specification,pageRequest);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public UserEntity updateUserById(Long userId, UserEntity updatedUser) {
        return userRepository.findById(userId).map(existingUser -> {
            if (updatedUser.getUsername() != null) existingUser.setUsername(updatedUser.getUsername());
            if (updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
            if (updatedUser.getUserRole() != null) existingUser.setUserRole(updatedUser.getUserRole());
            if (updatedUser.getUserStatus() != null) existingUser.setUserStatus(updatedUser.getUserStatus());
            return existingUser;
        }).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "User with id %d was not found".formatted(userId)));
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }
}