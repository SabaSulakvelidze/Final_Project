package org.example.final_project.model.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.final_project.model.entity.UserEntity;
import org.example.final_project.model.enums.Role;
import org.example.final_project.model.enums.UserStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String userName;
    private Role role;
    private UserStatus userStatus;

    public static UserResponse toUserResponse(UserEntity userEntity){
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userEntity.getId());
        userResponse.setUserName(userEntity.getUserName());
        userResponse.setRole(userEntity.getRole());
        userResponse.setUserStatus(userEntity.getUserStatus());
        return userResponse;
    }

}
