package org.ppdx.mercarry.web;

import org.junit.jupiter.api.Test;
import org.ppdx.mercarry.product.service.ProductService;
import org.ppdx.mercarry.user.service.CustomUserDetailsService;
import org.ppdx.mercarry.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MypageControllerTest {
		@Autowired
		private MockMvc mockMvc;

		@MockBean
		private UserService userService;

		@MockBean
		private CustomUserDetailsService customUserDetailsService;

		@MockBean
		private BCryptPasswordEncoder passwordEncoder;

		@MockBean
		private ProductService productService;

		@Test
		@WithMockUser
		void testMypagePage() throws Exception {
				mockMvc.perform(get("/mypage"))
								.andExpect(status().isOk())
								.andExpect(view().name("mypage/index"));
		}

		@Test
		void testMypagePageWhenNotLoggedIn() throws Exception {
				mockMvc.perform(get("/mypage"))
								.andExpect(status().is3xxRedirection());
		}

    @Test
    @WithMockUser
    public void testCreateProductSuccess() throws Exception {
        mockMvc.perform(post("/mypage/products")
                .param("name", "Sample Product")
                .param("price", "1000")
								.with(csrf())
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/mypage/products"));
    }

		@Test
		@WithMockUser
		public void testCreateProductWithInvalidParams() throws Exception {
				mockMvc.perform(post("/mypage/products")
								.param("name", "Sample Product")
								.param("price", "-1")
								.with(csrf())
				)
				.andExpect(status().isOk())
				.andExpect(view().name("mypage/products/new"))
				.andExpect(model().attributeHasFieldErrors("product", "price"));
		}

}
