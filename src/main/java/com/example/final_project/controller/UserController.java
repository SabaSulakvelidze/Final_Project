package com.example.final_project.controller;

import com.example.final_project.facade.UserFacade;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.enums.UserRole;
import com.example.final_project.model.enums.UserStatus;
import com.example.final_project.model.request.UserRequest;
import com.example.final_project.model.response.PagedResponse;
import com.example.final_project.model.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;


    @GetMapping("/getUser/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserResponse> getUser(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(userFacade.findUserById(userId), HttpStatus.OK);
    }

    @GetMapping("/getUsers")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<PagedResponse<UserResponse>> getUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) UserRole userRole,
            @RequestParam(required = false) UserStatus userStatus,

            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "id") String sortBy) {
        return new ResponseEntity<>(PagedResponse.of(userFacade.getUsers(
                username, email, userRole, userStatus,
                pageNumber, pageSize, direction, sortBy)), HttpStatus.OK);
    }

    @PutMapping("/update/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public UserResponse updateUser(@PathVariable("userId") Long userId,
                                   @RequestBody @Valid UserRequest userRequest) {
        return userFacade.updateUserById(userId, userRequest);
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId) {
        userFacade.deleteUserById(userId);
        return ResponseEntity.ok().build();
    }
}

