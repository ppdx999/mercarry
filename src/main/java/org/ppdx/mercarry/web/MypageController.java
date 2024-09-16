package org.ppdx.mercarry.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.ppdx.mercarry.product.service.ProductService;
import org.ppdx.mercarry.user.domain.User;

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
}
