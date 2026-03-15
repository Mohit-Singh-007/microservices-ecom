package com.micro.cart.services.impl;

import com.micro.cart.client.ProductClient;
import com.micro.cart.dto.AddToCartReq;
import com.micro.cart.dto.CartItemRes;
import com.micro.cart.dto.CartRes;
import com.micro.cart.dto.ProductRes;
import com.micro.cart.models.Cart;
import com.micro.cart.models.CartItems;
import com.micro.cart.repository.CartItemRepo;
import com.micro.cart.repository.CartRepo;
import com.micro.cart.services.CartServiceInterface;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements CartServiceInterface {

    private final CartRepo cartRepo;
    private final ProductClient productClient;
    private final CartItemRepo cartItemRepo;

    // later get it using userId -> get a specific cart of a user[userId]
    private Cart getOrCreateCart(String keycloakId){
        return cartRepo.findByKeycloakId(keycloakId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setKeycloakId(keycloakId);
                    return cartRepo.save(cart);
                });
    }


    @Override
    @Transactional
    public CartRes addToCart(Jwt jwt ,AddToCartReq req) {

        // user id extract
        String keycloakId = jwt.getSubject();

        // 1. validate product via FeignClient
        ProductRes product;
        try{
            product = productClient.getProductById(req.getProductId());
        }catch (FeignException.NotFound e){
            throw new EntityNotFoundException("Product not found with id: "+req.getProductId());
        }


        // 2. get or create cart of the user later[userId]
        Cart cart = getOrCreateCart(keycloakId);

        // 3. if product/item in cart -> increase quantity
       Optional<CartItems> existingItem = cartItemRepo.findByCartCartIdAndProductId(cart.getCartId(), req.getProductId());
       if(existingItem.isPresent()){
           CartItems item = existingItem.get();
           item.setQuantity(item.getQuantity() + req.getQuantity());
           cartItemRepo.save(item);
       }else{

           // 4. create new item
           CartItems item = new CartItems();
           item.setProductId(req.getProductId());
           item.setProductName(product.getName());
           item.setPrice(product.getPrice());
           item.setQuantity(req.getQuantity());
           item.setCart(cart);
           cartItemRepo.save(item);
       }


        return mapToCartRes(cart.getCartId());
    }


    @Override
    @Transactional
    public CartRes updateQuantity(Jwt jwt , Long productId , int quantity) {

        // extract id from jwt
        String keycloakId = jwt.getSubject();


        // get the cart
        Cart cart = getOrCreateCart(keycloakId);

        // check if product/item is in cart
        CartItems item = cartItemRepo.findByCartCartIdAndProductId(cart.getCartId(), productId)
                .orElseThrow(()-> new EntityNotFoundException("Product not in cart: "+productId));


        // quantity less than 0 -> remove item
        if(quantity <= 0){
            cartItemRepo.delete(item);
        }else{
            item.setQuantity(quantity);
            cartItemRepo.save(item);
        }

        return mapToCartRes(cart.getCartId());
    }

    @Override
    @Transactional
    public CartRes removeItem(Jwt jwt , Long productId) {
        // extract id
        String keycloakId = jwt.getSubject();
       Cart cart = getOrCreateCart(keycloakId);
       cartItemRepo.deleteByCartCartIdAndProductId(cart.getCartId(), productId);
       cartItemRepo.flush();
       return mapToCartRes(cart.getCartId());
    }

    @Override
    @Transactional(readOnly = true)
    public CartRes getCart(Jwt jwt) {
        Cart cart = getOrCreateCart(jwt.getSubject());
        return mapToCartRes(cart.getCartId());
    }

    @Override
    public void clearCart(Jwt jwt) {
        Cart cart = getOrCreateCart(jwt.getSubject());
        cart.getItems().clear();
        cartRepo.save(cart);
    }


    // cart find kro -> uske items ko map kro[CartItemRes] -> total calc
    // -> CartRes se map krdo
    private CartRes mapToCartRes(Long cartId){
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(()-> new EntityNotFoundException("Cart not found..."));

        List<CartItemRes> res = cart.getItems()
                .stream().map(this::mapToCartItemRes).toList();

        BigDecimal total = res.stream()
                .map(CartItemRes::getSubTotal)
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        CartRes r = new CartRes();
        r.setCartId(cart.getCartId());
        r.setItems(res);
        r.setTotal(total);
        r.setTotalItems(res.stream()
                .mapToInt(CartItemRes::getQuantity).sum()
        );
        return r;
    }

    private CartItemRes mapToCartItemRes(CartItems i){
        CartItemRes res = new CartItemRes();
        res.setCartItemId(i.getCartItemId());
        res.setProductId(i.getProductId());
        res.setProductName(i.getProductName());
        res.setQuantity(i.getQuantity());
        res.setPrice(i.getPrice());
        res.setSubTotal(i.getSubTotal());
        res.setCreatedAt(i.getCreatedAt());
        return res;
    }



}
