package org.ppdx.mercarry.product.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.*;

import java.math.BigDecimal;

import org.ppdx.mercarry.user.domain.User;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {
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
			DISABLED,
			ACTIVE,
			SOLD_OUT
	}
}
