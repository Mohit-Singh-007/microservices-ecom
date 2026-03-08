package com.micro.product.repository;

import com.micro.product.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> , JpaSpecificationExecutor<Category> {

    Optional<Category> findByName(String name);

    Page<Category> findAllByIsActiveTrue(Pageable pageable);

    Optional<Category> findByCategoryIdAndIsActiveTrue(Long id);

}