package com.micro.cart.repository;

import com.micro.cart.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart,Long> {
    // later findByUserId
    Optional<Cart> findByKeycloakId(String keycloakId);
}
