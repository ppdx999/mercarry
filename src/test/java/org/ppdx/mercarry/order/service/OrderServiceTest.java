package org.ppdx.mercarry.order.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ppdx.mercarry.order.domain.Order;
import org.ppdx.mercarry.order.repository.OrderRepository;
import org.ppdx.mercarry.payment.domain.Payment;
import org.ppdx.mercarry.payment.service.PaymentService;
import org.ppdx.mercarry.product.domain.Product;
import org.ppdx.mercarry.product.service.ProductService;
import org.ppdx.mercarry.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public class OrderServiceTest {

	@Mock
	private OrderRepository	orderRepository;

	@Mock
	private PaymentService paymentService;

	@Mock
	private ProductService productService;

	@InjectMocks
	private OrderService orderService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateOrder() {
		Product product = new Product();
		User buyer = new User();
		User seller = new User();
		product.setId(1L);
		product.setSupplier(seller);
		product.setPrice(BigDecimal.valueOf(100));
		buyer.setId(1L);
		seller.setId(2L);

		var order = orderService.createOrder(product, buyer);

		assertNotNull(order);
		assertEquals(product, order.getProduct());
		assertEquals(buyer, order.getBuyer());
		assertEquals(seller, order.getSeller());
		assertEquals(BigDecimal.valueOf(100), order.getPrice());
	}

	@Test
	void testPurchaseProduct() {
		Product product = new Product();
		User buyer = new User();
		User seller = new User();
		product.setId(1L);
		product.setSupplier(seller);
		product.setPrice(BigDecimal.valueOf(100));
		buyer.setId(1L);
		seller.setId(2L);

		Payment payment = new Payment();
		payment.setId(1L);
		Order order = new Order();
		order.setId(1L);

		when(paymentService.create(any(Order.class))).thenReturn(payment);

		var pair = orderService.purchaseProduct(product, buyer);

		assertNotNull(pair);
		assertNotNull(pair.getFirst());
		assertNotNull(pair.getSecond());

		verify(productService).markAsSoldOut(product);
		verify(paymentService).approve(payment);
	}
}
