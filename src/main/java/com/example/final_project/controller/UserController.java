package com.example.final_project.controller;

import com.example.final_project.facade.UserFacade;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.request.UserRequest;
import com.example.final_project.model.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;


    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserResponse> getAllUsers(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(UserResponse.toUserResponse(userFacade.findUserById(userId)), HttpStatus.OK);
    }

    @GetMapping("/getAllUsers")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> getAllUsers(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                          @RequestParam(defaultValue = "5") Integer pageSize,
                                                          @RequestParam(defaultValue = "ASC") Sort.Direction direction,
                                                          @RequestParam(defaultValue = "id") String sortBy) {
        return new ResponseEntity<>(userFacade.findAllUsers(pageNumber, pageSize, direction, sortBy).map(UserResponse::toUserResponse), HttpStatus.OK);
    }

    @PutMapping("/update/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public UserResponse updateUser(@PathVariable("userId") Long userId,
                                   @RequestBody UserRequest userRequest) {
        return UserResponse.toUserResponse(userFacade.updateUserById(userId, UserEntity.toUserEntity(userRequest)));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId) {
        userFacade.deleteUserById(userId);
        return ResponseEntity.ok().build();
    }
}

