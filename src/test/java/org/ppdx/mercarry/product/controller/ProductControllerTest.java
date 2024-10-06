package org.ppdx.mercarry.product.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.ppdx.mercarry.product.domain.ProductImage;
import org.ppdx.mercarry.product.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductImageService productImageService;

    @Test
    void testServeImage() throws Exception {
        ProductImage productImage = new ProductImage();
        productImage.setId(1l);
        productImage.setContentId("C1");
				productImage.setContentLength(1000);

				when(productImageService.findById(any(Long.class))).thenReturn(Optional.of(productImage));
				when(productImageService.getInputStream(any(ProductImage.class)))
					.thenReturn(new ByteArrayInputStream(new byte[1000]));

				mockMvc.perform(get("/product-images/1"))
						.andExpect(status().isOk())
						.andExpect(header().string("Content-Type", "image/jpeg"))
						.andExpect(header().longValue("Content-Length", 1000));
    }
}
