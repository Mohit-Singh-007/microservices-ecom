package com.micro.product.services.impl;

import com.micro.product.dto.productDTO.ProductReq;
import com.micro.product.dto.productDTO.ProductRes;
import com.micro.product.exceptions.CategoryNotFoundException;
import com.micro.product.models.Category;
import com.micro.product.models.Product;
import com.micro.product.repository.CategoryRepo;
import com.micro.product.repository.ProductRepo;
import com.micro.product.services.ProductServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductServiceInterface {

    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;

    @Override
    public ProductRes createProduct(ProductReq req) {

        Product p = new Product();
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setPrice(req.getPrice());
        p.setBrand(req.getBrand());
        p.setImageUrl(req.getImageUrl());

        // set sku
        String sku = "SKU-" + UUID.randomUUID().toString().substring(0,8);
        p.setSku(sku);

        // if category is provided , find it and use it
        if(req.getCategoryId() != null){
            Category c = categoryRepo.findById(req.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category does not exist..."));
            p.setCategory(c);
        }

        Product saved = productRepo.save(p);

        return mapToProductRes(saved);
    }

    @Override
    public List<ProductRes> getAllProducts() {
        return List.of();
    }

    @Override
    public ProductRes getProductById(Long productId) {
        return null;
    }

    @Override
    public void deleteProduct(Long productId) {

    }


    ProductRes mapToProductRes(Product p){
        ProductRes res = new ProductRes();
        res.setId(p.getProductId());
        res.setName(p.getName());
        res.setDescription(p.getDescription());
        res.setPrice(p.getPrice());
        res.setCategory(p.getCategory().getName());
        res.setBrand(p.getBrand());
        res.setSku(p.getSku());
        res.setImageUrl(p.getImageUrl());
        return res;
    }
}
