package org.ppdx.mercarry.product.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ppdx.mercarry.product.domain.Product;
import org.ppdx.mercarry.product.domain.ProductImage;
import org.ppdx.mercarry.product.repository.ProductRepository;
import org.ppdx.mercarry.user.domain.User;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.math.BigDecimal;

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
	void testGetProductsBySeller() {
		// Arrange
		User seller = new User();
		seller.setId(1L);

		Product product1 = new Product();
		product1.setId(1L);
		product1.setSeller(seller);

		when(productRepository.findBySeller(seller)).thenReturn(List.of(product1));

		// Act
		List<Product> products = productService.getProductsBySeller(seller);

		// Assert
		verify(productRepository).findBySeller(seller);
		assertThat(products.size()).isEqualTo(1);
		assertThat(products).containsExactly(product1);
	}

    @Test
    public void testCreateProduct() throws Exception {
        // Arrange
        MockitoAnnotations.openMocks(this);
        String name = "Test Product";
        BigDecimal price = new BigDecimal("100.00");
        User seller = new User();
        seller.setId(1L);
        MockMultipartFile imgFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "image content".getBytes());
        ProductImage productImage = new ProductImage();
        productImage.setId(1L);

        // Mock the behavior of repository and services
        when(productImageService.createProductImage(any(Product.class), any(MockMultipartFile.class))).thenReturn(productImage);

        // Act
        Product returnedProduct = productService.createProduct(name, price, seller, imgFile);

        // Assert returned product
        assertNotNull(returnedProduct);
        assertEquals(name, returnedProduct.getName());
        assertEquals(price, returnedProduct.getPrice());
        assertEquals(seller, returnedProduct.getSeller());
        assertNotNull(returnedProduct.getTopImage());

        // Assert saved product
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository, atLeastOnce()).save(productCaptor.capture());
        Product finalSavedProduct = productCaptor.getValue();

        assertNotNull(finalSavedProduct);
        assertEquals(name, finalSavedProduct.getName());
        assertEquals(price, finalSavedProduct.getPrice());
        assertEquals(seller, finalSavedProduct.getSeller());
        assertNotNull(finalSavedProduct.getTopImage());
        assertEquals(productImage, finalSavedProduct.getTopImage());
    }

		@Test
		void testMarkAsSoldOut() {
				// Arrange
				Product product = new Product(Product.Status.ON_SALE);

				// Act
				Product returnedProduct = productService.markAsSoldOut(product);

				// Assert
				assertEquals(Product.Status.SOLD_OUT, returnedProduct.getStatus());
				verify(productRepository).save(product);
		}
}
