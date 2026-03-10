package com.micro.user.repository;

import com.micro.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByKeycloakId(String id);
    Optional<User> findByEmail(String email);

    boolean existsByKeycloakId(String id);
}
