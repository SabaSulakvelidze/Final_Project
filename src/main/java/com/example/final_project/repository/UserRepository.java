package com.example.final_project.repository;

import com.example.final_project.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findFirstByUsernameEquals(String username);

    Optional<UserEntity> findFirstByEmail(String email);
}
