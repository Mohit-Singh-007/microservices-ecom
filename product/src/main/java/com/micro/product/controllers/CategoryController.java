package com.micro.product.controllers;

import com.micro.product.dto.categoryDTO.CategoryReq;
import com.micro.product.dto.categoryDTO.CategoryRes;
import com.micro.product.services.CategoryServiceInterface;
import com.micro.product.utils.PaginatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;


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

    @PatchMapping("/admin/{id}/status")
    public ResponseEntity<Void> softDeleteCategoryById(@PathVariable Long id){
        category.toggleCategoryStatus(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/admin")
    public ResponseEntity<PaginatedResponse<CategoryRes>> getCategoriesForAdmin(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10 , sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {


        Boolean isActive = null;

        // allow only specific fields to be sorted
        Set<String> allowed = Set.of("name","createdAt","isActive");
        for(Sort.Order order : pageable.getSort()){
            if(!allowed.contains(order.getProperty())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"invalid sort field...");
            }
        }


        if (status != null) {
            if (status.equalsIgnoreCase("active")) {
                isActive = true;
            } else if (status.equalsIgnoreCase("inactive")) {
                isActive = false;
            }
        }

        PaginatedResponse<CategoryRes> res =
                category.getCategoriesForAdmin(isActive, search, pageable);

        return ResponseEntity.ok(res);
    }

}
