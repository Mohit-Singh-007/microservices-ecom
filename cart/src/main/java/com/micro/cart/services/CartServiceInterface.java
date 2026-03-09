package com.micro.cart.services;

import com.micro.cart.dto.AddToCartReq;
import com.micro.cart.dto.CartRes;

public interface CartServiceInterface {
   CartRes addToCart(AddToCartReq req);
   CartRes updateQuantity(Long productId, int quantity);
   CartRes removeItem(Long productId);
   CartRes getCart();
   void clearCart();
}
