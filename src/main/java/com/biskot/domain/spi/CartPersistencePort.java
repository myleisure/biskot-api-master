package com.biskot.domain.spi;

import com.biskot.domain.model.Cart;

import java.util.Optional;

public interface CartPersistencePort {

    Optional<Cart> getCart(long id);

    void saveCart(Cart cart);
    
}
