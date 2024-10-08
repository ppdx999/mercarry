package org.ppdx.mercarry.user.config;

import org.ppdx.mercarry.user.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(login -> login.permitAll().defaultSuccessUrl("/", true)
                        .loginPage("/signin"))
                .logout(logout -> logout.logoutSuccessUrl("/").logoutUrl("/signout"))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                            "/",
                            "/signin",
                            "/signup",
                            "/products/**",
                            "/css/**",
                            "/js/**",
                            "/images/**",
                            "/product-images/**").permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }
}
