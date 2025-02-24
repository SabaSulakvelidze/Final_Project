package com.example.final_project.controller;


import com.example.final_project.exception.CustomException;
import com.example.final_project.facade.AuthFacade;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.enums.UserRole;
import com.example.final_project.model.request.UserRequest;
import com.example.final_project.model.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/signUp")
    public ResponseEntity<UserResponse> signUpUser(@RequestBody @Valid UserRequest userRequest) {
        if (userRequest.getRole().equals(UserRole.ADMIN)) throw new CustomException(HttpStatus.FORBIDDEN, "Admin has to be added manually");
        return new ResponseEntity<>(UserResponse.toUserResponse(authFacade.signUp(UserEntity.toUserEntity(userRequest))), HttpStatus.CREATED);
    }

    @PostMapping("/signIn")
    public ResponseEntity<String> logIn(@RequestParam("username") String username,
                                        @RequestParam("password") String password) {
        return new ResponseEntity<>(authFacade.signIn(username, password), HttpStatus.OK);
    }

    @PostMapping("/verify")
    public UserResponse verifyUser(@RequestParam("userEmail") String userEmail,
                                   @RequestParam("verificationCode") String verificationCode) {
        return UserResponse.toUserResponse(authFacade.verifyUser(userEmail, verificationCode));
    }

    @GetMapping("/resendVerificationCode")
    public ResponseEntity<String> resendVerificationCode(@RequestParam("userEmail") String userEmail) {
        authFacade.resendVerificationCode(userEmail);
        return ResponseEntity.status(HttpStatus.OK).body("Email with verification code was sent!");
    }

}
