package com.micro.product.services;

import com.micro.product.dto.productDTO.ProductReq;
import com.micro.product.dto.productDTO.ProductRes;
import com.micro.product.utils.PaginatedResponse;
import org.springframework.data.domain.Pageable;


public interface ProductServiceInterface {
    ProductRes createProduct(ProductReq product);
    PaginatedResponse<ProductRes> getAllProducts(Pageable pageable);
    ProductRes getProductById(Long productId);
    void deleteProduct(Long productId);
}
