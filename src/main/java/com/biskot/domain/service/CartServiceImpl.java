package com.biskot.domain.service;

import com.biskot.domain.model.Cart;
import com.biskot.domain.model.Product;
import com.biskot.domain.spi.CartPersistencePort;
import com.biskot.infra.gateway.ProductGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

	private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);
	
	private CartPersistencePort cartPersistencePort;
	
	private ProductGateway productGateway;
	
	private AtomicLong atomicLong = new AtomicLong(0);
	
	private static final int OLD_ENTITY = 1;
	private static final int NEW_ENTITY = 0;
	private static final BigDecimal TOTAL_PRICE = BigDecimal.valueOf(100);
	
	@Autowired
	public CartServiceImpl(CartPersistencePort cartPersistencePort, ProductGateway productGateway) {
		this.cartPersistencePort = cartPersistencePort;
		this.productGateway = productGateway;
	}

    @Override
    public void createCart() {
        long id = atomicLong.addAndGet(1);
        Cart cart = new Cart();
        cart.setId(id);
        cartPersistencePort.saveCart(cart);
    }

    @Override
    public Cart getCart(long cartId) {
        return cartPersistencePort.getCart(cartId)
				.orElseThrow(() -> {
					log.error("Cart with id {} is not found", cartId);
					return new RuntimeException("Cart not found");
				});
    }

    @Override
    public void addProductToCart(long cartId, long productId, int quantityToAdd) {
        Cart cart = getCart(cartId);
        
        Product stockReference = productGateway.getProduct(productId);
        if(stockReference == null) {
        	log.error("tock reference product doesn't exist for product {}", productId);
        	throw new RuntimeException("Stock reference product doesn't exist");
        }
        
        Product product = getProductFromCart(cart, productId);
        
        //Create new product if it doesn't exist yet
        if(product == null) {
        	log.warn("We are creating new product if it ");
			product = createNewProduct(productId, stockReference);
		}
        
        checkIfCartContainsMoreThan3Products(cart, product);

		checkIfQuantityExceedStockAvailability(product, stockReference, quantityToAdd);
        
        updateQuantityByStockAvailability(cart, product, quantityToAdd);
        
        checkIfCartTotalPriceExceeds100(cart);
        
        cartPersistencePort.saveCart(cart);
    }

	private Product createNewProduct(long productId, Product stockReference) {
		final var product = new Product();
		product.setId(productId);
		product.setQuantity(0);
		product.setUnitPrice(stockReference.getUnitPrice());
		product.setPersitedEntity(NEW_ENTITY);
		return product;
	}


	private void checkIfCartTotalPriceExceeds100(Cart cart) {
    	double totalCartPrice = cart.getProducts().stream()
    	                  					      .map(p -> p.getUnitPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
    	                  					      .collect(Collectors.summingDouble(BigDecimal::doubleValue));
    	final var totalCartPriceDecimal = BigDecimal.valueOf(totalCartPrice);
    	
    	if(totalCartPriceDecimal.compareTo(TOTAL_PRICE) > 0) {
    		log.error("Total price of the cart should not exceed {}", TOTAL_PRICE);
    		throw new RuntimeException("Total price of the cart should not exceed 100 euros");
    	}
    	
    }
    
    private void checkIfCartContainsMoreThan3Products(Cart cart, Product product) {
    	if(!CollectionUtils.isEmpty(cart.getProducts()) 
    			&& cart.getProducts().size() == 3 
    			&& product.getPersitedEntity() == NEW_ENTITY) {
			log.error("Cart cannot contain more than 3 different products");
    		throw new RuntimeException("Cart cannot contain more than 3 different products");
    	}
    }
    
    private void updateQuantityByStockAvailability(Cart cart,
												   Product product,
												   int quantityToAdd) {

		product.setQuantity(product.getQuantity() + quantityToAdd);
    	long productId = product.getId();
        	
        if(product.getPersitedEntity() == OLD_ENTITY) {
	        List<Product> newProductList = new ArrayList<>();
	        for(Product product2 : cart.getProducts()) {
	        	if(product2.getId() != productId) {
	        		newProductList.add(product2);
	        	} else {
	        		newProductList.add(product);
	        	}
	        }
	        cart.setProducts(newProductList);
        } else {
        	product.setPersitedEntity(OLD_ENTITY);
        	cart.getProducts().add(product);
        }
    }

	private void checkIfQuantityExceedStockAvailability(Product product, Product stockReference, int quantityToAdd) {
		if(product.getQuantity() + quantityToAdd > stockReference.getQuantity()) {
			throw new RuntimeException("Added quantity of a product should not exceed the stock availability");
		}
	}

	private Product getProductFromCart(Cart cart, long productId) {
    	Product product = null;
    	if(!CollectionUtils.isEmpty(cart.getProducts())) {
	    	product =  cart.getProducts().stream()
				.filter(p -> p.getId() == productId)
				.findFirst().orElse(null);
    	}
    	return product;
    }
}
