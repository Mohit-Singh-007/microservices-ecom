package com.micro.order.client;

import com.micro.order.dto.ProductRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "PRODUCT")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductRes getProductById(@PathVariable Long id);

    @PutMapping("/api/products/{id}/stock")
    void deductStock(@PathVariable Long id , @RequestParam int quantity,
    @RequestHeader("Authorization") String bearerToken);
}
