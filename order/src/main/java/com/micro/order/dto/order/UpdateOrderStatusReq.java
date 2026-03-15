package com.micro.order.dto.order;

import com.micro.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusReq {
    @NotNull(message = "Status is required")
    private OrderStatus status;
}
