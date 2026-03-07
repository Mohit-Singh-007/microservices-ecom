package com.micro.product.repository;


import com.micro.product.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {

    Page<Product> findAllByIsActiveTrue(Pageable pageable);
    Optional<Product> findByProductIdAndIsActiveTrue(Long productId);
}
