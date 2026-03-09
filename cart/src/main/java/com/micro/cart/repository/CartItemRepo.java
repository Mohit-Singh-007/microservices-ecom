package com.micro.cart.repository;

import com.micro.cart.models.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepo extends JpaRepository<CartItems,Long> {

    // specific product in a specific cart
    Optional<CartItems> findByCartCartIdAndProductId(Long cartId, Long productId);

    // delete specific product from a specific cart
//    @Modifying(clearAutomatically = true) // clears hibernate cache after delete or use flush() in service
//    @Transactional
    void deleteByCartCartIdAndProductId(Long cartId,Long productId);
}
