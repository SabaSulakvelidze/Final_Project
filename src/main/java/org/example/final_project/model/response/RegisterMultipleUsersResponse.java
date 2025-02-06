package org.example.final_project.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterMultipleUsersResponse {
    private List<UserResponse> registeredUsers;
    private List<String> errors;
}
