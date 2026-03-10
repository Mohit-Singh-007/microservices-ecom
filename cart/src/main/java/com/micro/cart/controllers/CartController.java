package com.micro.cart.controllers;

import com.micro.cart.dto.AddToCartReq;
import com.micro.cart.dto.CartRes;
import com.micro.cart.services.CartServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartServiceInterface cartServiceInterface;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartRes> getCart(){
        return ResponseEntity.ok(cartServiceInterface.getCart());
    }

    @PostMapping("/items")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartRes> addToCart(@Valid @RequestBody AddToCartReq req){
        return ResponseEntity.ok(cartServiceInterface.addToCart(req));
    }

    @PatchMapping("/items/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartRes> updateQuantity(@PathVariable Long productId,
                                                  @RequestParam int quantity
                                                  ){
        return ResponseEntity.ok(cartServiceInterface.updateQuantity(productId, quantity));
    }

    @DeleteMapping("/items/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartRes> deleteItem(@PathVariable Long productId){
        return ResponseEntity.ok(cartServiceInterface.removeItem(productId));
    }

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> clearCart(){
        cartServiceInterface.clearCart();
        return ResponseEntity.noContent().build();
    }
}
