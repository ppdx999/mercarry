package org.ppdx.mercarry.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.ppdx.mercarry.core.BusinessException;
import org.ppdx.mercarry.user.domain.User;
import org.ppdx.mercarry.user.repository.UserRepository;
import org.ppdx.mercarry.user.service.CustomUserDetailsService;
import org.ppdx.mercarry.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void testIndexPage() throws Exception {
        mockMvc.perform(get("/").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void testSigninPage() throws Exception {
        mockMvc.perform(get("/signin"))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"));
    }

    @Test
    void testWhenNotLoggedIn_thenSignInAndSignUpDisplayed() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(xpath("//a[@href='/signin']").exists())
                .andExpect(xpath("//a[@href='/signup']").exists());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testWhenLoggedIn_thenMypageLinkAndSignoutFormIsDisplayed() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(xpath("//a[@href='/mypage']").exists())
                .andExpect(xpath("//form[@action='/signout']").exists());
    }

    @Test
    void testSignupPage() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
    }

    @Test
    void testSignupUser() throws Exception {
        mockMvc.perform(post("/signup")
                .param("username", "testuser")
                .param("password", "password")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/signin"));

        verify(userService).registerNewUser("testuser", "password");
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "abc" })
    void testSignupUserFailWithLessThan4WordOfUsername(String username) throws Exception {
        mockMvc.perform(post("/signup")
                .param("username", username)
                .param("password", "password")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(xpath("//p[@class='invalid-feedback']")
                        .string("Username must be at least 4 characters"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "abcdefg" })
    void testSignupUserFailWithLessThan8WordOfPassword(String password) throws Exception {
        mockMvc.perform(post("/signup")
                .param("username", "testuser")
                .param("password", password)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(xpath("//p[@class='invalid-feedback']")
                        .string("Password must be at least 8 characters"));
    }

    @Test
    void testSignupUserFailWithExistingUsername() throws Exception {
        when(userService.registerNewUser("testuser", "password"))
                .thenThrow(new BusinessException("This username is already taken."));

        mockMvc.perform(post("/signup")
                .param("username", "testuser")
                .param("password", "password")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(xpath("//p[@class='text-danger']")
                        .string("This username is already taken."));
    }
}
