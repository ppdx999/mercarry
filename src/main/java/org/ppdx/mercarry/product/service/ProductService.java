package org.ppdx.mercarry.product.service;

import org.ppdx.mercarry.core.BusinessException;
import org.ppdx.mercarry.product.domain.Product;
import org.ppdx.mercarry.product.domain.ProductImage;
import org.ppdx.mercarry.product.repository.ProductRepository;
import org.ppdx.mercarry.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@Service
public class ProductService {
	@Autowired
	private ProductImageService productImageService;

	@Autowired
	private ProductRepository productRepository;

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public List<Product> getProductsBySeller(User seller) {
		return productRepository.findBySeller(seller);
	}

	public Optional<Product> getProductById(Long id) {
		return productRepository.findById(id);
	}

	public Product createProduct(String name, BigDecimal price, User seller, MultipartFile imgFile) throws Exception {
		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		product.setSeller(seller);
		productRepository.save(product);

		ProductImage productImage = productImageService.createProductImage(product, imgFile);

		product.setTopImage(productImage);
		productRepository.save(product);
		return product;
	}

	public Product markAsSoldOut(Product product) throws BusinessException {
		product.setStatus(Product.Status.SOLD_OUT);
		productRepository.save(product);
		return product;
	}

	public void saveProduct(Product product) {
		productRepository.save(product);
	}
}
