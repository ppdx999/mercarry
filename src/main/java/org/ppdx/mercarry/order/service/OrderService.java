package org.ppdx.mercarry.order.service;


import org.ppdx.mercarry.core.BusinessException;
import org.ppdx.mercarry.core.Pair;
import org.ppdx.mercarry.order.domain.Order;
import org.ppdx.mercarry.order.repository.OrderRepository;
import org.ppdx.mercarry.payment.domain.Payment;
import org.ppdx.mercarry.payment.service.PaymentService;
import org.ppdx.mercarry.product.domain.Product;
import org.ppdx.mercarry.product.service.ProductService;
import org.ppdx.mercarry.user.domain.User;
import org.ppdx.mercarry.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private UserService userService;

	private boolean canAfford(User user, Product product) {
		return userService.canAfford(user, product.getPrice());
	}

	@Transactional
	public Pair<Order, Payment> purchaseProduct(Product product, User buyer) throws BusinessException {
		if (!product.isOnSale()) {
			throw new BusinessException("Product is not on sale");
		}

		if (!canAfford(buyer, product)) {
			throw new BusinessException("Insufficient balance");
		}

		productService.markAsSoldOut(product);
		var order = createOrder(product, buyer);

		var payment = paymentService.create(order);
		paymentService.approve(payment);

		return Pair.of(order, payment);
	}

	public Order createOrder(Product product, User buyer) {
		Order order = new Order();
		order.setProduct(product);
		order.setBuyer(buyer);
		order.setSeller(product.getSeller());
		order.setPrice(product.getPrice());
		orderRepository.save(order);
		return order;
	}
}
