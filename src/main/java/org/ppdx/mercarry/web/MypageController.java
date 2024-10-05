package org.ppdx.mercarry.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.ppdx.mercarry.product.service.ProductService;
import org.ppdx.mercarry.user.domain.User;
import org.ppdx.mercarry.user.service.UserService;

import java.math.BigDecimal;

import org.ppdx.mercarry.product.domain.Product;

@Controller
public class MypageController extends BaseController {

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

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
	public String createProduct(
			@Validated @ModelAttribute Product product,
			BindingResult valid,
			@RequestParam("image") MultipartFile imgFile,
			@AuthenticationPrincipal User user) throws Exception {
		if (valid.hasErrors()) {
			return "mypage/products/new";
		}

		productService.createProduct(product.getName(), product.getPrice(), user, imgFile);
		return "redirect:/mypage/products";
	}

	@GetMapping("/mypage/wallet")
	public String wallet(Model model, @AuthenticationPrincipal User user) {
		model.addAttribute("wallet", user.getWallet());
		return "mypage/wallet/index";
	}

	@PostMapping("/mypage/wallet/charge")
	public String chargeWallet(@RequestParam("amount") BigDecimal amount, @AuthenticationPrincipal User user) {
		userService.chargeWallet(user, amount);
		return "redirect:/mypage/wallet";
	}
}
