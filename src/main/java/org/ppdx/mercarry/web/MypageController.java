package org.ppdx.mercarry.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.ppdx.mercarry.product.service.ProductService;
import org.ppdx.mercarry.user.domain.User;
import org.ppdx.mercarry.product.domain.Product;

@Controller
public class MypageController extends BaseController {

	@Autowired
	private ProductService productService;

	@GetMapping("/mypage")
	public String index(Model model) {
		return "mypage/index";
	}

	@GetMapping("/mypage/products")
	public String listProducts(Model model, @AuthenticationPrincipal User user) {
		model.addAttribute("products", productService.getProductsBySupplier(user));
		return "mypage/products/index";
	}

	@GetMapping("/mypage/products/new")
	public String newProduct(Model model) {
		model.addAttribute("product", new Product());
		return "mypage/products/new";
	}

	@PostMapping("/mypage/products")
	public String createProduct(@ModelAttribute Product product, BindingResult valid, @AuthenticationPrincipal User user) {
		product.setSupplier(user);
		productService.saveProduct(product);
		return "redirect:/mypage/products";
	}
}
