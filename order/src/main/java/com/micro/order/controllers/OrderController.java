package com.micro.order.controllers;

import com.micro.order.dto.order.OrderRes;
import com.micro.order.dto.order.PlaceOrderReq;
import com.micro.order.dto.order.UpdateOrderStatusReq;
import com.micro.order.services.OrderServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderServiceInterface orderService;

    // POST /api/orders  — place order from cart
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderRes> placeOrder(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody PlaceOrderReq req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(jwt, req));
    }

    // GET /api/orders  — user order history
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderRes>> getMyOrders(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(orderService.getAllOrders(jwt));
    }

    // GET /api/orders/{id}  — get specific order
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderRes> getOrderById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(jwt, id));
    }

    // DELETE /api/orders/{id}  — cancel order (user)
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderRes> cancelOrder(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(jwt, id));
    }

    // PATCH /api/orders/{id}/status  — update status (admin only)
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderRes> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusReq req) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, req));
    }

}
