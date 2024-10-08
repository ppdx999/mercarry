package org.ppdx.mercarry.product.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.*;

import java.math.BigDecimal;

import org.ppdx.mercarry.core.BusinessException;
import org.ppdx.mercarry.user.domain.User;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
public class Product {
	public Product(Status status) {
		this.status = status;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Length(min = 1)
	private String name;
	
	@Range(min = 100, max = 50000)
	private BigDecimal price;

	@ManyToOne
	@JoinColumn(name = "supplier_id")
	private User supplier;

	@OneToOne
	@JoinColumn(name = "top_image_id")
	private ProductImage topImage;

	@Enumerated(EnumType.STRING)
	private Status status;

	public enum Status {
			DISABLED {
				@Override
				public Status transitionTo(Status newStatus) {
					if (newStatus == Status.ON_SALE) {
						return Status.ON_SALE;
					} else if (newStatus == Status.DISABLED) {
						return Status.DISABLED;
					} else {
						throw new BusinessException("Invalid status transition: " + this + " -> " + newStatus);
					}
				}
			},
			ON_SALE {
				@Override
				public Status transitionTo(Status newStatus) {
					if (newStatus == Status.SOLD_OUT) {
						return Status.SOLD_OUT;
					} else if (newStatus == Status.ON_SALE) {
						return Status.ON_SALE;
					} else if (newStatus == Status.DISABLED) {
						return Status.DISABLED;
					} else {
						throw new BusinessException("Invalid status transition: " + this + " -> " + newStatus);
					}
				}
			},
			SOLD_OUT {
				@Override
				public Status transitionTo(Status newStatus) {
					if (newStatus == Status.SOLD_OUT) {
						return Status.SOLD_OUT;
					} else {
						throw new BusinessException("Invalid status transition: " + this + " -> " + newStatus);
					}
				}
			};

			public abstract Status transitionTo(Status newStatus);
	}
	
	public void setStatus(Status status) throws BusinessException {
		this.status = this.status.transitionTo(status);
	}

	public boolean isSoldOut() {
		return status == Status.SOLD_OUT;
	}
}
