package org.ppdx.mercarry.web;

import org.ppdx.mercarry.product.domain.Product;
import org.ppdx.mercarry.product.service.ProductService;
import org.ppdx.mercarry.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController extends BaseController {

    @Autowired
    private UserService userService;

		@Autowired
		private ProductService productService;

    @GetMapping("/")
    public String index(Model model) {
				List<Product> products = productService.getAllProducts();
				model.addAttribute("products", products);
        return "index";
    }

    @GetMapping("/signin")
    public String signin() {
        return "signin";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signupUser(@RequestParam String username, @RequestParam String password) {
        userService.registerNewUser(username, password);
        return "redirect:/signin";
    }
}
