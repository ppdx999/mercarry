package org.ppdx.mercarry.payment.service;

import org.ppdx.mercarry.order.domain.Order;
import org.ppdx.mercarry.payment.domain.Payment;
import org.ppdx.mercarry.payment.repository.PaymentRepository;
import org.ppdx.mercarry.user.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {
	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private WalletService walletService;

	public Payment create(Order order) {
		Payment payment = new Payment();
		payment.setProduct(order.getProduct());
		payment.setOrder(order);
		payment.setReceiver(order.getSeller());
		payment.setSender(order.getBuyer());
		payment.setAmount(order.getPrice());
		payment.setStatus(Payment.Status.PENDING);
		paymentRepository.save(payment);
		return payment;
	}

	@Transactional
	public void approve(Payment payment) {
		walletService.transfer(payment.getSender().getWallet(), payment.getReceiver().getWallet(), payment.getAmount());
		payment.setStatus(Payment.Status.APPROVED);
		paymentRepository.save(payment);
	}

}
