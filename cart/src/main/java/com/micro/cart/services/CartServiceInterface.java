package com.micro.cart.services;

import com.micro.cart.dto.AddToCartReq;
import com.micro.cart.dto.CartRes;
import org.springframework.security.oauth2.jwt.Jwt;

public interface CartServiceInterface {
   CartRes addToCart(Jwt jwt ,AddToCartReq req);
   CartRes updateQuantity(Jwt jwt ,Long productId, int quantity);
   CartRes removeItem(Jwt jwt,Long productId);
   CartRes getCart(Jwt jwt);
   void clearCart(Jwt jwt);
}
