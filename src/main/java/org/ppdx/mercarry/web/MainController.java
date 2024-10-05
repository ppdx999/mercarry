package org.ppdx.mercarry.web;

import org.ppdx.mercarry.core.BusinessException;
import org.ppdx.mercarry.product.domain.Product;
import org.ppdx.mercarry.product.service.ProductService;
import org.ppdx.mercarry.user.domain.User;
import org.ppdx.mercarry.user.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

import java.util.List;

@Controller
public class MainController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private Logger logger;

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
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        try {
            userService.registerNewUser(user.getUsername(), user.getPassword());
            return "redirect:/signin";
        } catch (BusinessException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "signup";
        } catch (Exception e) {
            logger.error("An unexpected error occurred.", e);
            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            return "signup";
        }
    }
}
