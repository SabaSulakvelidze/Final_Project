package org.example.final_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.final_project.model.response.ShopResponse;
import org.example.final_project.service.UserService;
import org.example.final_project.model.entity.UserEntity;
import org.example.final_project.model.enums.Role;
import org.example.final_project.model.enums.UserStatus;
import org.example.final_project.model.request.UserRequest;
import org.example.final_project.model.response.RegisterMultipleUsersResponse;
import org.example.final_project.model.response.UserResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/Users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostConstruct
    public void initAdminUser(){
        UserEntity user = new UserEntity();
        user.setUserName("admin");
        user.setPassword("pas123456");
        user.setRole(Role.ADMIN);
        user.setUserStatus(UserStatus.ACTIVE);
        userService.registerUser(user);
    }

    @PostMapping("/RegisterUser")
    public UserResponse registerUser(@RequestBody @Valid UserRequest userRequest) {
        return UserResponse.toUserResponse(userService.registerUser(UserEntity.toUserEntity(userRequest)));
    }

    @PostMapping("/RegisterMultipleUser")
    public RegisterMultipleUsersResponse registerMultipleUser(@RequestBody @Valid List<UserRequest> userRequests) {
        List<UserResponse> successUsers = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        userRequests.forEach(userRequest -> {
            try {
                successUsers.add(UserResponse.toUserResponse(userService.registerUser(UserEntity.toUserEntity(userRequest))));
            }catch (Exception e) {
                errors.add("Failed to register user: " + userRequest.getUserName());
            }
        });
        return new RegisterMultipleUsersResponse(successUsers,errors);
    }

    @PostMapping("/login")
    public Object login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
       return userService.logIn(username, password);
    }

    @PutMapping("/changeUserStatus/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    //@SecurityRequirement(name = "bearerAuth")
    public UserResponse changeUserStatus(@PathVariable("userId") Long userId,
                                         @RequestParam("userStatus")UserStatus userStatus) {
       return UserResponse.toUserResponse(userService.changeUserStatus(userId,userStatus));
    }

    @GetMapping("/getAllUsers")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Page<UserResponse> getAllUsers(@RequestParam(defaultValue = "0") Integer pageNumber,
                                          @RequestParam(defaultValue = "5") Integer pageSize,
                                          @RequestParam(defaultValue = "ASC") Sort.Direction direction,
                                          @RequestParam(defaultValue = "id") String sortBy){
        return userService.getAllUsers(pageNumber, pageSize, direction, sortBy).map(UserResponse::toUserResponse);
    }

}

