package org.ppdx.mercarry.product.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ppdx.mercarry.product.domain.Product;
import org.ppdx.mercarry.product.domain.ProductImage;
import org.ppdx.mercarry.product.repository.ProductRepository;
import org.ppdx.mercarry.user.domain.User;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

public class ProductServiceTest {
	@Mock
	private ProductRepository productRepository;

	@Mock
	private ProductImageService productImageService;

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

	@Test
	void testCreateProduct() throws Exception {
		// Arrange
		User supplier = new User();
		supplier.setId(1L);

		Product product = new Product();
		product.setId(1L);

		MockMultipartFile imgFile = new MockMultipartFile("images", "test.jpg", "image/jpeg", "test".getBytes());
		ProductImage productImage = new ProductImage();
		when(productImageService.createProductImage(product, imgFile)).thenReturn(productImage);

		// Act
		productService.createProduct(product, supplier, imgFile);

		// Assert
		verify(productRepository, times(2)).save(product);
		verify(productImageService).createProductImage(product, imgFile);
		assertThat(product.getSupplier()).isEqualTo(supplier);
		assertThat(product.getTopImage()).isEqualTo(productImage);
	}
}
