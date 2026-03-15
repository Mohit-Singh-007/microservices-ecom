package com.micro.order.services;

import com.micro.order.dto.order.OrderRes;
import com.micro.order.dto.order.PlaceOrderReq;
import com.micro.order.dto.order.UpdateOrderStatusReq;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface OrderServiceInterface {
    public OrderRes cancelOrder(Jwt jwt , Long orderId);
    public OrderRes updateOrderStatus(Long orderId , UpdateOrderStatusReq req);
    public OrderRes getOrderById(Jwt jwt, Long orderId);
    public OrderRes placeOrder(Jwt jwt , PlaceOrderReq req);
    public List<OrderRes> getAllOrders(Jwt jwt);
}
