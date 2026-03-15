package com.micro.order.dto.order;

import com.micro.order.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRes {
    private Long orderId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String street;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private List<OrderItemRes> items;
    private LocalDateTime createdAt;
}
