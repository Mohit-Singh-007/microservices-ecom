package com.micro.product.controllers;

import com.micro.product.dto.productDTO.ProductReq;
import com.micro.product.dto.productDTO.ProductRes;
import com.micro.product.services.ProductServiceInterface;
import com.micro.product.utils.PaginatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {


    private final ProductServiceInterface product;

    @PostMapping
    public ResponseEntity<ProductRes> createProduct(@Valid @RequestBody ProductReq req){
        ProductRes p = product.createProduct(req);
        return ResponseEntity.ok(p);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<ProductRes>> getAllProducts(
           @PageableDefault(size = 10, sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable
    ){
        PaginatedResponse<ProductRes> res = product.getAllProducts(pageable);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductRes> getProductById(@PathVariable Long id){
        ProductRes p = product.getProductById(id);
        return ResponseEntity.ok(p);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Void> softDeleteCategoryById(@PathVariable Long id){
        product.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
