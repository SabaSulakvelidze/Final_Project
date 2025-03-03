package com.example.final_project.model.specification;

import com.example.final_project.model.entity.UserEntity;
import com.example.final_project.model.enums.UserRole;
import com.example.final_project.model.enums.UserStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<UserEntity> searchUsers(String username, String email, UserRole userRole, UserStatus userStatus){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (username != null && !username.isEmpty()) predicates.add(criteriaBuilder.like(root.get("username"), "%" + username.toLowerCase() + "%"));
            if (email != null && !email.isEmpty()) predicates.add(criteriaBuilder.like(root.get("email"), "%" + email.toLowerCase() + "%"));
            if (userRole != null) predicates.add(criteriaBuilder.equal(root.get("userRole"), userRole));
            if (userStatus != null) predicates.add(criteriaBuilder.equal(root.get("userStatus"), userStatus));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
