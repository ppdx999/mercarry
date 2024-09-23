package org.ppdx.mercarry.product.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ppdx.mercarry.product.domain.Product;
import org.ppdx.mercarry.product.domain.ProductImage;
import org.ppdx.mercarry.product.repository.ProductImageContentStore;
import org.ppdx.mercarry.product.repository.ProductImageRepository;
import org.springframework.mock.web.MockMultipartFile;

public class ProductImageServiceTest {
    @Mock
    private ProductImageRepository productImageRepository;

    @Mock
    private ProductImageContentStore productImageContentStore;

    @InjectMocks
    private ProductImageService productImageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProductImage() throws Exception {
        // Arrange
        Product product = new Product();
        MockMultipartFile imgFile = new MockMultipartFile("imgFile", "test.jpg", "image/jpeg", "test".getBytes());

        // Act
        ProductImage productImage = productImageService.createProductImage(product, imgFile);

        // Assert
        assertThat(productImage.getMimeType()).isEqualTo("image/jpeg");
        assertThat(productImage.getProduct()).isEqualTo(product);
    }
}
