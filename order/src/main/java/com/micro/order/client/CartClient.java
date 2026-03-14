package com.micro.order.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "CART")
public interface CartClient {
}
