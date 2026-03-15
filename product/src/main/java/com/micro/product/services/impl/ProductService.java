package com.micro.product.services.impl;

import com.micro.product.dto.productDTO.ProductReq;
import com.micro.product.dto.productDTO.ProductRes;
import com.micro.product.exceptions.CategoryNotFoundException;
import com.micro.product.exceptions.InsufficientStockException;
import com.micro.product.exceptions.ProductNotFoundException;
import com.micro.product.models.Category;
import com.micro.product.models.Product;
import com.micro.product.repository.CategoryRepo;
import com.micro.product.repository.ProductRepo;
import com.micro.product.repository.specifications.ProductSpecification;
import com.micro.product.services.ProductServiceInterface;
import com.micro.product.utils.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductServiceInterface {

    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;

    @Override
    @Transactional
    public ProductRes createProduct(ProductReq req) {

        Product p = new Product();
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setPrice(req.getPrice());
        p.setBrand(req.getBrand());
        p.setImageUrl(req.getImageUrl());
        p.setStock(req.getStock());

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
    public PaginatedResponse<ProductRes> getAllProducts(Pageable pageable) {

        Page<Product> p = productRepo.findAllByIsActiveTrue(pageable);

        List<ProductRes> res = p.stream()
                        .map(this::mapToProductRes).toList();

        return new PaginatedResponse<>(res,p);

    }

    @Override
    public ProductRes getProductById(Long productId) {
        Product p = productRepo.findByProductIdAndIsActiveTrue(productId)
                .orElseThrow(()->new ProductNotFoundException("Product not found with id: "+productId));

        return mapToProductRes(p);
    }

    @Override
    public PaginatedResponse<ProductRes> getProductsForAdmin(Boolean status, String search, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Specification<Product> spec = ProductSpecification.withFilters(status,search,categoryId,minPrice,maxPrice);

        Page<Product> p = productRepo.findAll(spec,pageable);

        List<ProductRes> res = p.stream().map(this::mapToProductRes).toList();

        return new PaginatedResponse<>(res,p);

    }

    @Override
    public void toggleProductStatus(Long productId) {

        Product p = productRepo.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException("Product not found with id: "+productId));

        p.setActive(!p.isActive());
        productRepo.save(p);
    }


    @Override
    @Transactional
    public void deductStock(Long productId, int s){
        Product p = productRepo.findById(productId)
                .orElseThrow(()->new ProductNotFoundException("Product with id "+productId+" does not exist..."));

        int updatedStock = p.getStock() - s;

        // -ve -> restore stock[on order cancel]
        if(updatedStock < 0){
            throw new InsufficientStockException("Out of stock...");
        }
        p.setStock(updatedStock);
        productRepo.save(p);

    }


    ProductRes mapToProductRes(Product p){
        ProductRes res = new ProductRes();
        res.setId(p.getProductId());
        res.setName(p.getName());
        res.setDescription(p.getDescription());
        res.setPrice(p.getPrice());
        res.setBrand(p.getBrand());
        res.setSku(p.getSku());
        res.setImageUrl(p.getImageUrl());
        res.setActive(p.isActive());
        res.setCreatedAt(p.getCreatedAt());
        res.setStock(p.getStock());

        res.setCategory(
                p.getCategory() != null && p.getCategory().isActive()
                ? p.getCategory().getName()
                : "Uncategorized"
        );
        return res;
    }
}
