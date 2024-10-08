package	org.ppdx.mercarry.order.controller;

import org.ppdx.mercarry.core.BusinessException;
import org.ppdx.mercarry.order.service.OrderService;
import org.ppdx.mercarry.product.service.ProductService;
import org.ppdx.mercarry.user.domain.User;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {
	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;

	@Autowired
	private Logger logger;

	@PostMapping("")
	public String purchaseProduct(Long productId, @AuthenticationPrincipal User user, Model model) {
		var optionalProduct = productService.getProductById(productId);

		if (optionalProduct.isEmpty()) {
			// TODO: show toast error message to the user.
			// Error message: "Sorry, the product is not found."
			return "redirect:/?error";
		}

		try {
			orderService.purchaseProduct(optionalProduct.get(), user);
			return "redirect:/";
		} catch (BusinessException e) {
			logger.error("BusinessException: Failed to purchase the product", e);
			// TODO: show toast error message to the user.
			return "redirect:/?error";
		} catch (Exception e) {
			logger.error("Exception: Failed to purchase the product", e);
			model.addAttribute("errorMessage", "Failed to purchase the product due to an unexpected error.");
			// TODO: show toast error message to the user.
			return "redirect:/?error";
		}
	}
}
