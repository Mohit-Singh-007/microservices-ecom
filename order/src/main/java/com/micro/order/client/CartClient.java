package com.micro.order.client;

import com.micro.order.dto.CartRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "CART")
public interface CartClient {

    @GetMapping("/api/cart")
    CartRes getCart(@RequestHeader("Authorization")String token);

    @DeleteMapping("/api/cart")
    void clearCart(@RequestHeader("Authorization")String token);

}
