package org.ppdx.mercarry.product.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

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

	private String name;
	
	@Range(min = 100, max = 50000)
	private BigDecimal price;

	@ManyToOne
	@JoinColumn(name = "supplier_id")
	private User supplier;
}
