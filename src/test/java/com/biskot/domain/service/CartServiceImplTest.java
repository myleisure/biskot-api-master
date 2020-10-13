package com.biskot.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import com.biskot.domain.model.Cart;
import com.biskot.domain.model.Product;
import com.biskot.infra.gateway.ProductGateway;
import com.biskot.infra.repository.InMemoryCartRepository;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {
	
	@Spy
	private InMemoryCartRepository cartPersistencePort;
	
	@Mock
	private ProductGateway productGateway;
	
	@InjectMocks
	private CartServiceImpl cartServiceImp;
	
	@Test
	void createAndGetCart_Succes() {
		cartServiceImp.createCart();
		Cart cart = cartServiceImp.getCart(1l);
		
		assertNotNull(cart);
		assertEquals(1, cart.getId());
		assertNull(cart.getProducts());	
	}
	
	@Test
	void createAndGetCart_IdNotMatch() {
		cartServiceImp.createCart();
		assertThrowsMethod(RuntimeException.class, () -> cartServiceImp.getCart(100l), "Cart not found");
	}
	
	@Test
	void addProductToCart_ReferenceProductNotExist() {
		Product product = createProduct(1, "Blond product", 400, BigDecimal.valueOf(5));

		cartServiceImp.createCart();
		
		when(productGateway.getProduct(1)).thenReturn(product);
		
		Cart cart = cartServiceImp.getCart(1l);
		cartServiceImp.addProductToCart(cart.getId(), 1, 20);
		
		assertThrowsMethod(RuntimeException.class, 
						   () -> cartServiceImp.addProductToCart(cart.getId(), 5, 20), 
						   "Stock reference product doesn't exist");
	}
	
	@Test
	void addProductToCart_NormaTestCase_1() {
		Product product = createProduct(1, "Blond product", 400, BigDecimal.valueOf(5));

		cartServiceImp.createCart();
		
		when(productGateway.getProduct(anyLong())).thenReturn(product);
		
		Cart cart = cartServiceImp.getCart(1l);
		cartServiceImp.addProductToCart(cart.getId(), 2, 20);
		
		cart = cartServiceImp.getCart(1l);
		assertCartEntity(cart, 1l, 1l, 100);
	}
	
	@Test
	void addProductToCart_NormaTestCase_2() {
		Product product1 = createProduct(1, "Blond product 1", 400, BigDecimal.valueOf(5));

		Product product2 = createProduct(2, "Blond product 2", 200, BigDecimal.valueOf(5));

		Product product3 = createProduct(3, "Blond product 3", 200, BigDecimal.valueOf(5));

		List<Product> lProductsMock = List.of(product1, product2, product3);
		
		cartServiceImp.createCart();
		
		when(productGateway.getProduct(anyLong())).thenAnswer(new Answer<Product>() {
			@Override
			public Product answer(InvocationOnMock invocation) throws Throwable {
				long param = invocation.getArgument(0, Long.class);
				return lProductsMock.get((int)param - 1);
			}
		});
		
		Cart cart = cartServiceImp.getCart(1l);
		cartServiceImp.addProductToCart(cart.getId(), 1, 5);
		cartServiceImp.addProductToCart(cart.getId(), 2, 5);
		cartServiceImp.addProductToCart(cart.getId(), 3, 5);

		
		cart = cartServiceImp.getCart(1l);
		assertCartEntity(cart, 1l, 3l, 75);
	}
	
	@Test
	void addProductToCart_NormaTestCase_3() {
		Product product1 = createProduct(1, "Blond product 1", 400, BigDecimal.valueOf(5));

		Product product2 = createProduct(2, "Blond product 2", 200, BigDecimal.valueOf(5));

		Product product3 = createProduct(3, "Blond product 3", 200, BigDecimal.valueOf(5));

		List<Product> lProductsMock = List.of(product1, product2, product3);
		
		cartServiceImp.createCart();
		
		when(productGateway.getProduct(anyLong())).thenAnswer(new Answer<Product>() {
			@Override
			public Product answer(InvocationOnMock invocation) throws Throwable {
				long param = invocation.getArgument(0, Long.class);
				return lProductsMock.get((int)param - 1);
			}
		});
		
		Cart cart = cartServiceImp.getCart(1l);
		cartServiceImp.addProductToCart(cart.getId(), 1, 5);
		cartServiceImp.addProductToCart(cart.getId(), 2, 5);
		cartServiceImp.addProductToCart(cart.getId(), 3, 5);
		
		//Add another quantity of product 3
		cartServiceImp.addProductToCart(cart.getId(), 3, 5);


		
		cart = cartServiceImp.getCart(1l);
		assertCartEntity(cart, 1l, 3l, 100);
	}
	
	@Test
	void addProductToCart_ContainsMoreThan3Products() {
		Product product1 = createProduct(1, "Blond product 1", 400, BigDecimal.valueOf(5));

		Product product2 = createProduct(2, "Blond product 2", 200, BigDecimal.valueOf(5));

		Product product3 = createProduct(3, "Blond product 2", 200, BigDecimal.valueOf(5));

		Product product4 = createProduct(4, "Blond product 4", 200, BigDecimal.valueOf(5));

		List<Product> lProductsMock = List.of(product1, product2, product3, product4);
		
		cartServiceImp.createCart();
		
		when(productGateway.getProduct(anyLong())).thenAnswer(new Answer<Product>() {
			@Override
			public Product answer(InvocationOnMock invocation) throws Throwable {
				long param = invocation.getArgument(0, Long.class);
				return lProductsMock.get((int)param - 1);
			}
		});
		
		Cart cart = cartServiceImp.getCart(1l);
		cartServiceImp.addProductToCart(cart.getId(), 1, 5);
		cartServiceImp.addProductToCart(cart.getId(), 2, 5);
		cartServiceImp.addProductToCart(cart.getId(), 3, 5);
		
		assertThrowsMethod(RuntimeException.class, 
				   () -> cartServiceImp.addProductToCart(cart.getId(), 4, 5), 
				   "Cart cannot contain more than 3 different products");
	}



	@Test
	void addProductToCart_TotalPriceNotExceed100() {
		Product product1 = createProduct(1, "Blond product 1", 400, BigDecimal.valueOf(5));

		Product product2 = createProduct(2, "Blond product 2", 200, BigDecimal.valueOf(5));

		Product product3 = createProduct(3, "Blond product 3", 200, BigDecimal.valueOf(5));

		List<Product> lProductsMock = List.of(product1, product2, product3);
		
		cartServiceImp.createCart();
		
		when(productGateway.getProduct(anyLong())).thenAnswer(new Answer<Product>() {
			@Override
			public Product answer(InvocationOnMock invocation) throws Throwable {
				long param = invocation.getArgument(0, Long.class);
				return lProductsMock.get((int)param - 1);
			}
		});
		
		Cart cart = cartServiceImp.getCart(1l);
		cartServiceImp.addProductToCart(cart.getId(), 1, 5);
		cartServiceImp.addProductToCart(cart.getId(), 2, 5);
		cartServiceImp.addProductToCart(cart.getId(), 3, 5);
		
		//Add another quantity of product 3
		cartServiceImp.addProductToCart(cart.getId(), 3, 5);
		
		assertThrowsMethod(RuntimeException.class, 
				   () -> cartServiceImp.addProductToCart(cart.getId(), 1, 5), 
				   "Total price of the cart should not exceed 100 euros");
	}
	
	@Test
	void addProductToCart_QuantityShouldNotExceedStock() {
		Product product1 = createProduct(1, "Blond product 1", 400, BigDecimal.valueOf(0.25));

		List<Product> lProductsMock = List.of(product1);
		
		cartServiceImp.createCart();
		
		when(productGateway.getProduct(anyLong())).thenAnswer(new Answer<Product>() {
			@Override
			public Product answer(InvocationOnMock invocation) throws Throwable {
				long param = invocation.getArgument(0, Long.class);
				return lProductsMock.get((int)param - 1);
			}
		});
		
		Cart cart = cartServiceImp.getCart(1l);
		cartServiceImp.addProductToCart(cart.getId(), 1, 50);
		cartServiceImp.addProductToCart(cart.getId(), 1, 200);
		cartServiceImp.addProductToCart(cart.getId(), 1, 100);

		
		assertThrowsMethod(RuntimeException.class, 
				   () -> cartServiceImp.addProductToCart(cart.getId(), 1, 51),
				   "Added quantity of a product should not exceed the stock availability");
	}
	
	@Test
	void addProductToCart_CartDoesntExist() {
		Product product1 = createProduct(1, "Blond product 1", 400, BigDecimal.valueOf(0.25));

		List<Product> lProductsMock = List.of(product1);
		
		cartServiceImp.createCart();
		
		when(productGateway.getProduct(anyLong())).thenAnswer(new Answer<Product>() {
			@Override
			public Product answer(InvocationOnMock invocation) throws Throwable {
				long param = invocation.getArgument(0, Long.class);
				return lProductsMock.get((int)param - 1);
			}
		});
		
		Cart cart = cartServiceImp.getCart(1l);
		cartServiceImp.addProductToCart(cart.getId(), 1, 50);
		
		assertThrowsMethod(RuntimeException.class,
				() -> cartServiceImp.addProductToCart(50, 1, 100), "Cart not found");

	}
	
	
	private <T extends Throwable> void assertThrowsMethod(Class<T> expectedType, Executable executable, String expectedMessage) {
		T exception = assertThrows(expectedType, executable);
		
		assertNotNull(exception);
		assertTrue(exception.getMessage().contains(expectedMessage));
	}
	
	private void assertCartEntity(Cart cart, long expectedId, long expectedSizeProduct, double expectedPrice) {
		assertNotNull(cart);
		assertEquals(expectedId, cart.getId());
		assertEquals(expectedSizeProduct, cart.getProducts().size());
		
    	double totalCartPrice = cart.getProducts().stream()
			      .map(p -> p.getUnitPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
			      .collect(Collectors.summingDouble(BigDecimal::doubleValue));
											
		assertEquals(expectedPrice, totalCartPrice);
	}

	private Product createProduct(int id, String label, int quantity, BigDecimal price) {
		Product product1 = new Product();
		product1.setId(id);
		product1.setLabel(label);
		product1.setQuantity(quantity);
		product1.setUnitPrice(price);
		return product1;
	}

}