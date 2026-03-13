package com.micro.cart.controllers;

import com.micro.cart.dto.AddToCartReq;
import com.micro.cart.dto.CartRes;
import com.micro.cart.services.CartServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CartController {
    private final CartServiceInterface cartServiceInterface;

    @GetMapping
    public ResponseEntity<CartRes> getCart(@AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok(cartServiceInterface.getCart(jwt));
    }

    @PostMapping("/items")
    public ResponseEntity<CartRes> addToCart(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody AddToCartReq req){
        return ResponseEntity.ok(cartServiceInterface.addToCart(jwt,req));
    }

    @PatchMapping("/items/{productId}")
    public ResponseEntity<CartRes> updateQuantity(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long productId,
            @RequestParam int quantity
    ){
        return ResponseEntity.ok(cartServiceInterface.updateQuantity(jwt,productId, quantity));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartRes> deleteItem(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long productId){
        return ResponseEntity.ok(cartServiceInterface.removeItem(jwt,productId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal Jwt jwt){
        cartServiceInterface.clearCart(jwt);
        return ResponseEntity.noContent().build();
    }
}
