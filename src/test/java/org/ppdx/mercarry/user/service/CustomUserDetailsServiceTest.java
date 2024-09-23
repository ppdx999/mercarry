package org.ppdx.mercarry.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ppdx.mercarry.user.domain.Role;
import org.ppdx.mercarry.user.domain.User;
import org.ppdx.mercarry.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

public class CustomUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        // Arrange
        User user = new User();
        user.setUsername("test");
        user.setPassword("encodedPassword");
        user.setRoles(Set.of(new Role(1L, "USER")));
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test");

        // Assert
        assertThat(userDetails.getUsername()).isEqualTo("test");
        assertThat(userDetails.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void testLoadUserByUsernameFailure() {
        // Arrange
        when(userRepository.findByUsername("test")).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("test"))
                .isInstanceOf(org.springframework.security.core.userdetails.UsernameNotFoundException.class);
    }
}
