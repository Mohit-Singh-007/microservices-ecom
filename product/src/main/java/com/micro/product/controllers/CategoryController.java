package com.micro.product.controllers;

import com.micro.product.dto.categoryDTO.CategoryReq;
import com.micro.product.dto.categoryDTO.CategoryRes;
import com.micro.product.services.CategoryServiceInterface;
import com.micro.product.utils.PaginatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryServiceInterface category;

    @PostMapping
    public ResponseEntity<CategoryRes> createCategory(@Valid @RequestBody CategoryReq req){
       CategoryRes c = category.createCategory(req);
       return ResponseEntity.status(HttpStatus.CREATED).body(c);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<CategoryRes>> getAllCategories(
            @PageableDefault(size = 10 , sort = "createdAt" , direction = Sort.Direction.DESC) Pageable pageable){
        PaginatedResponse<CategoryRes> res = category.getAllCategories(pageable);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryRes> getCategoryById(@PathVariable Long id){
        CategoryRes res = category.getCategoryById(id);
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> softDeleteCategoryById(@PathVariable Long id){
        category.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }

}
