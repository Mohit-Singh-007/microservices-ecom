package com.micro.product.services;

import com.micro.product.dto.productDTO.ProductReq;
import com.micro.product.dto.productDTO.ProductRes;

import java.util.List;

public interface ProductServiceInterface {
    ProductRes createProduct(ProductReq product);
    List<ProductRes> getAllProducts();
    ProductRes getProductById(Long productId);
    void deleteProduct(Long productId);
}
