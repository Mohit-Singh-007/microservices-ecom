package com.micro.order.repository;

import com.micro.order.models.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<CustomerOrder,Long> {
    List<CustomerOrder> findByUserIdOrderByCreatedAtDesc(String userId);
    Optional<CustomerOrder> findByOrderIdAndUserId(Long orderId, String userId);
}
