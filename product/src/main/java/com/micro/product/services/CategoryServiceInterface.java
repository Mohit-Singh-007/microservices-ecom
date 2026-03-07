package com.micro.product.services;

import com.micro.product.dto.categoryDTO.CategoryReq;
import com.micro.product.dto.categoryDTO.CategoryRes;
import com.micro.product.utils.PaginatedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryServiceInterface {
    CategoryRes createCategory(CategoryReq req);
    PaginatedResponse<CategoryRes> getAllCategories(Pageable pageable);
    CategoryRes getCategoryById(Long id);

    void deleteCategoryById(Long id);
}
