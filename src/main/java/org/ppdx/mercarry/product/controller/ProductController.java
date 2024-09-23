package org.ppdx.mercarry.product.controller;

import org.ppdx.mercarry.product.domain.ProductImage;
import org.ppdx.mercarry.product.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

@Controller
public class ProductController {

    @Autowired
    private ProductImageService productImageService;

    @GetMapping("/product-images/{imageId}")
    @ResponseBody
    public ResponseEntity<Resource> serveImage(@PathVariable Long imageId) {
        ProductImage productImage = productImageService.findById(imageId).get();
        InputStreamResource content = new InputStreamResource(productImageService.getInputStream(productImage));

        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg")
                .contentLength(productImage.getContentLength())
                .body(content);
    }
}
