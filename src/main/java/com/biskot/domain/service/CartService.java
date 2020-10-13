package com.biskot.domain.service;

import com.biskot.domain.model.Cart;

public interface CartService {

    void createCart();

    Cart getCart(long cartId);

    void addProductToCart(long cartId, long productId, int quantityToAdd);
}
