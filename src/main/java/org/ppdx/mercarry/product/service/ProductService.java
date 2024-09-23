package org.ppdx.mercarry.product.service;

import org.ppdx.mercarry.product.domain.Product;
import org.ppdx.mercarry.product.domain.ProductImage;
import org.ppdx.mercarry.product.repository.ProductRepository;
import org.ppdx.mercarry.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductService {
	@Autowired
	private ProductImageService productImageService;

	@Autowired
	private ProductRepository productRepository;

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public List<Product> getProductsBySupplier(User supplier) {
		return productRepository.findBySupplier(supplier);
	}

	public void createProduct(Product product, User supplier, MultipartFile imgFile) throws Exception {
		product.setSupplier(supplier);
		productRepository.save(product);

		ProductImage productImage = productImageService.createProductImage(product, imgFile);
		product.setTopImage(productImage);
		saveProduct(product);
	}

	public void saveProduct(Product product) {
		productRepository.save(product);
	}
}
