package org.example.final_project.exception;

public class UserPermissionException extends RuntimeException{
    public UserPermissionException(String message) {
        super(message);
    }
}
