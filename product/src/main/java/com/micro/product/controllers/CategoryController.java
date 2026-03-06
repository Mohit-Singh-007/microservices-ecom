package com.micro.product.controllers;

import com.micro.product.dto.categoryDTO.CategoryReq;
import com.micro.product.dto.categoryDTO.CategoryRes;
import com.micro.product.services.CategoryServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryServiceInterface category;

    @PostMapping
    public CategoryRes createProduct(@RequestBody @Valid CategoryReq req){
       return category.createCategory(req);
    }

    @GetMapping
    public List<CategoryRes> getAllCategories(){
        return category.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryRes getCategoryById(@PathVariable Long id){
        return category.getCategoryById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCategoryById(@PathVariable Long id){
        category.deleteCategoryById(id);
    }

}
