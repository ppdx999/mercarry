package org.ppdx.mercarry.product.service;

import org.ppdx.mercarry.product.domain.Product;
import org.ppdx.mercarry.product.domain.ProductImage;
import org.ppdx.mercarry.product.repository.ProductImageContentStore;
import org.ppdx.mercarry.product.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.io.InputStream;

@Service
public class ProductImageService {
    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductImageContentStore productImageContentStore;

    public Optional<ProductImage> findById(Long id) {
        return productImageRepository.findById(id);
    }

    public InputStream getInputStream(ProductImage productImage) {
        return productImageContentStore.getContent(productImage);
    }

    public ProductImage createProductImage(Product product, MultipartFile imgFile) throws Exception {
        ProductImage productImage = new ProductImage();
        productImage.setProduct(product);
        productImage.setMimeType(imgFile.getContentType());

        productImageContentStore.setContent(productImage, imgFile.getInputStream());
        productImageRepository.save(productImage);

        return productImage;
    }
}
