package com.biskot.infra.repository;

import com.biskot.domain.model.Cart;
import com.biskot.domain.model.Product;
import com.biskot.domain.spi.CartPersistencePort;
import com.biskot.infra.repository.entity.CartEntity;
import com.biskot.infra.repository.entity.ProductEntity;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
public class InMemoryCartRepository implements CartPersistencePort {
	
	private ConcurrentHashMap<Long, CartEntity> cartMap = new ConcurrentHashMap<>();

    @Override
    public Optional<Cart> getCart(long id) {
    	if(cartMap.containsKey(id)) {
			return Optional.of(mapToCart(cartMap.get(id)));
    	}
    	return Optional.empty();

    }

	@Override
    public void saveCart(Cart cart) {
    	
    	CartEntity cartEntity = cartMap.get(cart.getId());
    	
    	if(cartEntity == null) {
        	cartEntity = new CartEntity();
        	cartEntity.setId(cart.getId());
    	}

		if(!CollectionUtils.isEmpty(cart.getProducts())) {
			cartEntity.setProductEntities(cart.getProducts().stream()
					.map(ProductEntity::new)
					.collect(Collectors.toList()));
		}
    	
    	cartMap.put(cart.getId(), cartEntity);
    }
    
	private Cart mapToCart(CartEntity cartEntity) {
		final var cart = new Cart();
		cart.setId(cartEntity.getId());
		if(!CollectionUtils.isEmpty(cartEntity.getProductEntities())) {
			cart.setProducts(cartEntity.getProductEntities().stream()
					.map(Product::new)
					.collect(Collectors.toList()));
		}
		return cart;
	}
}
