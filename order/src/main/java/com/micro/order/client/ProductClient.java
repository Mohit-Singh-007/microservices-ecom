package com.micro.order.client;

import com.micro.order.dto.ProductRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PRODUCT")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductRes getProductById(@PathVariable Long id);

    @PutMapping("/api/products/{id}/stock")
    void deductStock(@PathVariable Long id , @RequestParam int quantity);
}
