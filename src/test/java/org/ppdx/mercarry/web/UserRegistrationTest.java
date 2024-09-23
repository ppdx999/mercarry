package org.ppdx.mercarry.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ppdx.mercarry.user.domain.User;
import org.ppdx.mercarry.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
public class UserRegistrationTest {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user);

        System.out.println(userRepository.findByUsername("testuser"));
        if (userRepository.findByUsername("testuser") == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches("password", userRepository.findByUsername("testuser").get().getPassword())) {
            throw new RuntimeException("Password does not match");
        }
    }

    @Test
    void testSignup() throws Exception {
        try (final WebClient webClient = new WebClient()) {
            // disable JavaScript because htmlunit throws errors when loading bootstrap.min.js
            webClient.getOptions().setJavaScriptEnabled(false);

            HtmlPage page = webClient.getPage("http://localhost:8080/signup");

            HtmlForm form = page.getHtmlElementById("signupForm");

            // assert that the form contains a CSRF token
            HtmlInput csrfInput = form.getInputByName("_csrf");
            assertThat(csrfInput).isNotNull();

            // type username and password
            HtmlTextInput usernameInput = form.getInputByName("username");
            usernameInput.setValueAttribute("testuser");

            HtmlPasswordInput passwordInput = form.getInputByName("password");
            passwordInput.setValueAttribute("password");

            // submit form
            HtmlButton submitButton = form.getButtonByName("submit");
            HtmlPage resultPage = submitButton.click();

            // after submitting the form, check if the user is redirected to the success page
            assertThat(resultPage.getUrl().toString()).isEqualTo("http://localhost:8080/signin");
        }
    }
}
