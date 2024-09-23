package org.ppdx.mercarry.product.repository;

import org.ppdx.mercarry.product.domain.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}