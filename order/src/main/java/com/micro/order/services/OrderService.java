package com.micro.order.services;

import com.micro.order.client.CartClient;
import com.micro.order.client.ProductClient;
import com.micro.order.dto.CartItemRes;
import com.micro.order.dto.CartRes;
import com.micro.order.dto.ProductRes;
import com.micro.order.dto.order.OrderItemRes;
import com.micro.order.dto.order.OrderRes;
import com.micro.order.dto.order.PlaceOrderReq;
import com.micro.order.dto.order.UpdateOrderStatusReq;
import com.micro.order.enums.OrderStatus;
import com.micro.order.exceptions.EmptyCartException;
import com.micro.order.exceptions.InsufficientStockException;
import com.micro.order.exceptions.Invalidstatustransitionexception;
import com.micro.order.exceptions.OrderNotFoundException;
import com.micro.order.models.Order;
import com.micro.order.models.OrderItems;
import com.micro.order.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService implements OrderServiceInterface{

    private final CartClient cartClient;
    private final ProductClient productClient;
    private final OrderRepo orderRepo;


    private static final java.util.Map<OrderStatus, Set<OrderStatus>> VALID_TRANSITIONS =
            java.util.Map.of(
                    OrderStatus.PENDING,    Set.of(OrderStatus.CONFIRMED, OrderStatus.CANCELLED),
                    OrderStatus.CONFIRMED,  Set.of(OrderStatus.SHIPPED, OrderStatus.CANCELLED),
                    OrderStatus.SHIPPED,    Set.of(OrderStatus.DELIVERED),
                    OrderStatus.DELIVERED,  Set.of(),
                    OrderStatus.CANCELLED,  Set.of()
            );

    @Override
   public OrderRes placeOrder(Jwt jwt , PlaceOrderReq req){
        String keycloakId = jwt.getSubject();
        String token = "Bearer "+jwt.getTokenValue();

        // cart fetch kr
        CartRes cart = cartClient.getCart(token);
        if(cart.getItems() == null || cart.getItems().isEmpty()){
            throw new EmptyCartException("Cart is empty...");
        }

        // check stock for each item and deduct accordingly
        for(CartItemRes i : cart.getItems()){
            ProductRes product = productClient.getProductById(i.getProductId());
            if(product.getStock() < i.getQuantity()){
                throw new InsufficientStockException("Out of stock...");
            }

            productClient.deductStock(i.getProductId(), i.getQuantity());
        }

        // order create krle
        Order order = new Order();
        order.setKeycloakId(keycloakId);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setStreet(req.getStreet());
        order.setCity(req.getCity());
        order.setState(req.getState());
        order.setPincode(req.getPincode());
        order.setCountry(req.getCountry());

        // order items ko map krle to its OrderItemRes
        List<OrderItems> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItems oi = new OrderItems();
            oi.setProductId(cartItem.getProductId());
            oi.setProductName(cartItem.getProductName());
            oi.setPrice(cartItem.getPrice());
            oi.setQuantity(cartItem.getQuantity());
            oi.setSubTotal(cartItem.getSubTotal());
            oi.setOrder(order);
            return oi;
        }).toList();


        order.setItems(orderItems);
        order.setTotalAmount(cart.getTotal());
        Order saved = orderRepo.save(order);
        log.info("Order {} placed for user {}", saved.getOrderId(), keycloakId);

        cartClient.clearCart(token);

        return mapToOrderRes(saved);

    }


    @Override
    @Transactional(readOnly = true)
    public List<OrderRes> getAllOrders(Jwt jwt) {
        String keycloakId = jwt.getSubject();
        return orderRepo.findByKeycloakIdAndOrderByCreatedAtDesc(keycloakId)
                .stream()
                .map(this::mapToOrderRes)
                .toList();
    }



    @Override
    @Transactional(readOnly = true)
    public OrderRes getOrderById(Jwt jwt, Long orderId) {
        String keycloakId = jwt.getSubject();
        Order order = orderRepo.findByOrderIdAndKeycloakId(orderId, keycloakId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id: "+orderId+" not found..."));
        return mapToOrderRes(order);
    }


@Override
    @Transactional
    public OrderRes cancelOrder(Jwt jwt , Long orderId){
        String keycloakId = jwt.getSubject();
        Order order = orderRepo.findByOrderIdAndKeycloakId(orderId,keycloakId)
                .orElseThrow(()->new OrderNotFoundException("Order with id: "+orderId+" does not exist..."));


        validateTransition(order.getOrderStatus() , OrderStatus.CANCELLED);

        // restock krr
        order.getItems().forEach(i ->
                productClient.deductStock(i.getProductId(), -i.getQuantity())
                );

        order.setOrderStatus(OrderStatus.CANCELLED);
        log.info("Order {} cancelled by user {}" , orderId , keycloakId);

        return mapToOrderRes(orderRepo.save(order));

    }

    // update order status - ADMIN
    @Override
    public OrderRes updateOrderStatus(Long orderId , UpdateOrderStatusReq req){
        Order order = orderRepo.findById(orderId)
                .orElseThrow(()->new OrderNotFoundException("Order not found..."));

        validateTransition(order.getOrderStatus() , req.getStatus());
        order.setOrderStatus(req.getStatus());
        log.info("Order status {} updated to {}" , orderId,req.getStatus());

        return mapToOrderRes(orderRepo.save(order));
    }


    private void validateTransition(OrderStatus from, OrderStatus to) {
        if (!VALID_TRANSITIONS.get(from).contains(to)) {
            throw new Invalidstatustransitionexception("Invalid order status transition...");
        }
    }


    private OrderRes mapToOrderRes(Order order) {
        OrderRes res = new OrderRes();
        res.setOrderId(order.getOrderId());
        res.setStatus(order.getOrderStatus());
        res.setTotalAmount(order.getTotalAmount());
        res.setStreet(order.getStreet());
        res.setCity(order.getCity());
        res.setState(order.getState());
        res.setPincode(order.getPincode());
        res.setCountry(order.getCountry());
        res.setCreatedAt(order.getCreatedAt());
        res.setItems(order.getItems().stream().map(i -> {
            OrderItemRes oi = new OrderItemRes();
            oi.setOrderItemId(i.getOrderItemId());
            oi.setProductId(i.getProductId());
            oi.setProductName(i.getProductName());
            oi.setPrice(i.getPrice());
            oi.setQuantity(i.getQuantity());
            oi.setSubTotal(i.getSubTotal());
            return oi;
        }).toList());
        return res;
    }

}
