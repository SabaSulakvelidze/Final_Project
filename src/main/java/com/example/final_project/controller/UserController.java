package com.example.final_project.controller;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.enums.UserRole;
import com.example.final_project.model.enums.UserStatus;
import com.example.final_project.model.request.UserRequest;
import com.example.final_project.model.response.UserResponse;
import com.example.final_project.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostConstruct
    public void initAdminUser() {
        userService.registerUser(UserEntity.builder()
                .username("admin")
                .password("123456")
                .email("sabasualakvelidze@gmail.com")
                .role(UserRole.ADMIN)
                .userStatus(UserStatus.ACTIVE)
                .build());
    }

    @PostMapping("/RegisterUser")
    public UserResponse registerUser(@RequestBody @Valid UserRequest userRequest) {
        return UserResponse.toUserResponse(userService.registerUser(UserEntity.toUserEntity(userRequest)));
    }

    @PostMapping("/login")
    public Object login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        return userService.logIn(username, password);
    }

    @PutMapping("/changeUserStatus/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public UserResponse changeUserStatus(@PathVariable("userId") Long userId,
                                         @RequestParam("userStatus") UserStatus userStatus) {
        return UserResponse.toUserResponse(userService.changeUserStatus(userId, userStatus));
    }

    @GetMapping("/getAllUsers")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Page<UserResponse> getAllUsers(@RequestParam(defaultValue = "0") Integer pageNumber,
                                          @RequestParam(defaultValue = "5") Integer pageSize,
                                          @RequestParam(defaultValue = "ASC") Sort.Direction direction,
                                          @RequestParam(defaultValue = "id") String sortBy) {
        return userService.getAllUsers(pageNumber, pageSize, direction, sortBy).map(UserResponse::toUserResponse);
    }

}

