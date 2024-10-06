package org.ppdx.mercarry.order.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.ppdx.mercarry.user.domain.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.ppdx.mercarry.payment.domain.Payment;
import org.ppdx.mercarry.product.domain.Product;

@Entity
@Getter
@Setter
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "buyer_id")
	private User buyer;

	@ManyToOne
	@JoinColumn(name = "seller_id")
	private User seller;

	@OneToOne(mappedBy = "order")
	private Payment payment;

	@Range(min = 0)
	private BigDecimal price;

	@CreatedDate
	private LocalDateTime createdAt;
}
