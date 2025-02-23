package com.example.final_project.controller;


import com.example.final_project.facade.AuthFacade;
import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.enums.UserRole;
import com.example.final_project.model.enums.UserStatus;
import com.example.final_project.model.request.UserRequest;
import com.example.final_project.model.response.UserResponse;
import com.example.final_project.service.MailSenderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MailSenderService mailSenderService;

    private final AuthFacade authFacade;

    @PostMapping("/signUp")
    public ResponseEntity<UserResponse> signUpUser(@RequestBody @Valid UserRequest userRequest) {
        return new ResponseEntity<>(UserResponse.toUserResponse(authFacade.signUp(UserEntity.toUserEntity(userRequest))),
                HttpStatus.CREATED);
    }

    @PostMapping("/logIn")
    public ResponseEntity<String> logIn(@RequestParam("username") String username,
                                        @RequestParam("password") String password) {
        return new ResponseEntity<>(authFacade.logIn(username, password), HttpStatus.OK);
    }

    @PutMapping("/activateUser/{userId}")
    public UserResponse activateUser(@PathVariable("userId") Long userId,
                                     @RequestParam("verificationCode") String verificationCode) {
        return UserResponse.toUserResponse(authFacade.activateUser(userId, verificationCode));
    }

}
