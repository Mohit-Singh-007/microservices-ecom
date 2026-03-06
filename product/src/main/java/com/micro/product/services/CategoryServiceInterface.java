package com.micro.product.services;

import com.micro.product.dto.categoryDTO.CategoryReq;
import com.micro.product.dto.categoryDTO.CategoryRes;

import java.util.List;

public interface CategoryServiceInterface {
    CategoryRes createCategory(CategoryReq req);
    List<CategoryRes> getAllCategories();
    CategoryRes getCategoryById(Long id);

    void deleteCategoryById(Long id);
}
