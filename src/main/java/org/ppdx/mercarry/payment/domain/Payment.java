package org.ppdx.mercarry.payment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.ppdx.mercarry.user.domain.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.ppdx.mercarry.order.domain.Order;
import org.ppdx.mercarry.product.domain.Product;

@Entity
@Getter
@Setter
@Table(name = "payments")
@EntityListeners(AuditingEntityListener.class)
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@OneToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne
	@JoinColumn(name = "receiver_id")
	private User receiver;

	@ManyToOne
	@JoinColumn(name = "sender_id")
	private User sender;

	@Range(min = 0)
	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	private Status status;

	public enum Status {
			PENDING,
			APPROVED,
			CANCELLED
	}

	@CreatedDate
	private LocalDateTime createdAt;
}
