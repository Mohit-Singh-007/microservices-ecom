package com.micro.cart.client;

import com.micro.cart.dto.ProductRes;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRODUCT") // must be same as eureka
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductRes getProductById(@Valid @PathVariable Long id);

}
