package org.ppdx.mercarry.product.repository;

import org.ppdx.mercarry.product.domain.Product;
import org.ppdx.mercarry.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findBySupplier(User supplier);
}
