package com.micro.order.repository;

import com.micro.order.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order,Long> {
    List<Order> findByKeycloakIdAndOrderByCreatedAtDesc(String keycloakId);

    Optional<Order> findByOrderIdAndKeycloakId(Long orderId , String keycloakId);
}
