package org.ppdx.mercarry.product.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ppdx.mercarry.product.domain.Product;
import org.ppdx.mercarry.product.repository.ProductRepository;
import org.ppdx.mercarry.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

public class ProductServiceTest {
	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetProductsBySupplier() {
		// Arrange
		User supplier = new User();
		supplier.setId(1L);

		Product product1 = new Product();
		product1.setId(1L);
		product1.setSupplier(supplier);

		when(productRepository.findBySupplier(supplier)).thenReturn(List.of(product1));

		// Act
		List<Product> products = productService.getProductsBySupplier(supplier);

		// Assert
		verify(productRepository).findBySupplier(supplier);
		assertThat(products.size()).isEqualTo(1);
		assertThat(products).containsExactly(product1);
	}
}
