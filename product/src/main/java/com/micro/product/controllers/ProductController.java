package com.micro.product.controllers;

import com.micro.product.dto.productDTO.ProductReq;
import com.micro.product.dto.productDTO.ProductRes;
import com.micro.product.services.ProductServiceInterface;
import com.micro.product.utils.PaginatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {


    private final ProductServiceInterface product;

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
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

    @PatchMapping("/admin/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> softDeleteCategoryById(@PathVariable Long id){
        product.toggleProductStatus(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaginatedResponse<ProductRes>> getAllProductsAdmin(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(size = 10,sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable
    ){

        Boolean isActive=null;
        if(status != null){
            if(status.equalsIgnoreCase("active")) isActive=true;
            else if (status.equalsIgnoreCase("inactive")) isActive=false;
        }

        // limit the pageable sort
        Set<String> allowed = Set.of("name","price","createdAt","isActive");
        for(Sort.Order order : pageable.getSort()){

            if(!allowed.contains(order.getProperty())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"invalid sort field...");
            }
        }

        PaginatedResponse<ProductRes> res = product.getProductsForAdmin(isActive,search,categoryId,minPrice,maxPrice,pageable);
        return ResponseEntity.ok(res);
    }


}
