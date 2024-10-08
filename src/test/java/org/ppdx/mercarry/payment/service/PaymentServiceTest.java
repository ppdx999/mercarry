package org.ppdx.mercarry.payment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ppdx.mercarry.order.domain.Order;
import org.ppdx.mercarry.payment.domain.Payment;
import org.ppdx.mercarry.payment.repository.PaymentRepository;
import org.ppdx.mercarry.product.domain.Product;
import org.ppdx.mercarry.user.domain.User;
import org.ppdx.mercarry.user.domain.Wallet;
import org.ppdx.mercarry.user.repository.WalletRepository;
import org.ppdx.mercarry.user.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

public class PaymentServiceTest {
	@Mock
	private PaymentRepository paymentRepository;

	@Mock
	private WalletService	walletService;

	@InjectMocks
	private PaymentService paymentService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreate() {
		User buyer = new User();
		User seller = new User();
		Product product = new Product();
		Order order = new Order();
		order.setProduct(product);
		order.setBuyer(buyer);
		order.setSeller(seller);
		order.setPrice(BigDecimal.valueOf(100));

		var payment = paymentService.create(order);
		

		assertNotNull(payment);
		assertEquals(product, payment.getProduct());
		assertEquals(order, payment.getOrder());
		assertEquals(seller, payment.getReceiver());
		assertEquals(buyer, payment.getSender());
		assertEquals(BigDecimal.valueOf(100), payment.getAmount());
		assertEquals(Payment.Status.PENDING, payment.getStatus());
	}

	@Test
	void testApprove() {
		User sender = new User();
		Wallet senderWallet = new Wallet();
		senderWallet.setBalance(BigDecimal.valueOf(1000));
		sender.setWallet(senderWallet);

		User receiver = new User();
		Wallet receiverWallet = new Wallet();
		receiverWallet.setBalance(BigDecimal.valueOf(10));
		receiver.setWallet(receiverWallet);

		Payment payment = new Payment();
		payment.setSender(sender);
		payment.setReceiver(receiver);
		payment.setAmount(BigDecimal.valueOf(100));

		paymentService.approve(payment);

		verify(walletService).transfer(senderWallet, receiverWallet, BigDecimal.valueOf(100));
		assertEquals(Payment.Status.APPROVED, payment.getStatus());
	}
}
