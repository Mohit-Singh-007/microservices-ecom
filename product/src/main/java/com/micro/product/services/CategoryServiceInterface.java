package com.micro.product.services;

import com.micro.product.dto.categoryDTO.CategoryReq;
import com.micro.product.dto.categoryDTO.CategoryRes;
import com.micro.product.utils.PaginatedResponse;
import org.springframework.data.domain.Pageable;


public interface CategoryServiceInterface {
    CategoryRes createCategory(CategoryReq req);
    PaginatedResponse<CategoryRes> getAllCategories(Pageable pageable);
    CategoryRes getCategoryById(Long id);

    PaginatedResponse<CategoryRes> getCategoriesForAdmin(Boolean status,String search,Pageable pageable);

    void toggleCategoryStatus(Long id);
}
