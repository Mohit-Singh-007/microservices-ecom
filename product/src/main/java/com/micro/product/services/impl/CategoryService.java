package com.micro.product.services.impl;

import com.micro.product.dto.categoryDTO.CategoryReq;
import com.micro.product.dto.categoryDTO.CategoryRes;
import com.micro.product.models.Category;
import com.micro.product.repository.CategoryRepo;
import com.micro.product.services.CategoryServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceInterface {


    private final CategoryRepo categoryRepo;

    @Override
    public CategoryRes createCategory(CategoryReq req) {
        Category c = new Category();
        c.setName(req.getName());
        c.setDescription(req.getDescription());

        Category saved = categoryRepo.save(c);
        return mapToCategoryRes(saved);
    }

    @Override
    public List<CategoryRes> getAllCategories() {
       return categoryRepo.findAll().stream()
                .map(this::mapToCategoryRes).toList();
    }

    @Override
    public CategoryRes getCategoryById(Long id) {
        Category c = categoryRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Cannot find category..."));

        return mapToCategoryRes(c);
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepo.deleteById(id);
    }

    CategoryRes mapToCategoryRes(Category c){
        CategoryRes res = new CategoryRes();
        res.setId(c.getCategoryId());
        res.setName(c.getName());
        res.setDescription(c.getDescription());
        return res;
    }
}
