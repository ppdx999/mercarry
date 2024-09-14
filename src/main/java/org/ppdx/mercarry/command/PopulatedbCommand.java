package org.ppdx.mercarry.command;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import org.ppdx.mercarry.product.domain.Product;
import org.ppdx.mercarry.product.repository.ProductRepository;
import org.ppdx.mercarry.user.domain.User;
import org.ppdx.mercarry.user.repository.UserRepository;
import org.ppdx.mercarry.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class PopulatedbCommand implements Command {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public void run(String... args) throws Exception {
		User supplier = getOrCreateSupplier();
		getOrCreateProducts(supplier);
	}

	private User getOrCreateSupplier() {
		return userRepository.findByUsername("supplier").orElseGet(() -> userService.registerNewUser("supplier", "password"));
	}


	private List<Product> getOrCreateProducts(User supplier) {
		if (productRepository.findBySupplier(supplier).size() > 0) {
			return productRepository.findBySupplier(supplier);
		}

		Product product1 = new Product();
		product1.setName("Sample Product 1");
		product1.setPrice(BigDecimal.valueOf(100));
		product1.setSupplier(supplier);
		productRepository.save(product1);

		Product product2 = new Product();
		product2.setName("Sample Product 2");
		product2.setPrice(BigDecimal.valueOf(200));
		product2.setSupplier(supplier);
		productRepository.save(product2);

		return List.of(product1, product2);
	}
}
