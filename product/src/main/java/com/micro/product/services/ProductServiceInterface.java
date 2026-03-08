package com.micro.product.services;

import com.micro.product.dto.categoryDTO.CategoryRes;
import com.micro.product.dto.productDTO.ProductReq;
import com.micro.product.dto.productDTO.ProductRes;
import com.micro.product.utils.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;


public interface ProductServiceInterface {
    ProductRes createProduct(ProductReq product);
    PaginatedResponse<ProductRes> getAllProducts(Pageable pageable);
    ProductRes getProductById(Long productId);

    PaginatedResponse<ProductRes> getProductsForAdmin(
            Boolean status, String search, Long categoryId,
            BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    void toggleProductStatus(Long productId);
}
