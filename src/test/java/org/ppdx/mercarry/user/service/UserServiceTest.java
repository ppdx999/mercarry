package org.ppdx.mercarry.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ppdx.mercarry.core.BusinessException;
import org.ppdx.mercarry.user.domain.Role;
import org.ppdx.mercarry.user.domain.User;
import org.ppdx.mercarry.user.domain.Wallet;
import org.ppdx.mercarry.user.repository.RoleRepository;
import org.ppdx.mercarry.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterNewUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(new Role(1L, "USER")));

        userService.registerNewUser("testuser", "testpassword");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getUsername()).isEqualTo("testuser");
        assertThat(capturedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(capturedUser.isEnabled()).isTrue();
        assertThat(capturedUser.getRoles())
                .extracting(Role::getName)
                .containsExactly("USER");
    }

    @Test
    void testRegisterNewUserFailWithDuplicateUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.registerNewUser("testuser", "testpassword"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("This username is already taken.");

        verify(userRepository, never()).save(any(User.class));
    }

		@Test
		void testCanAfford() {
			User user = new User();
			Wallet wallet = new Wallet();
			wallet.setBalance(BigDecimal.valueOf(100));
			user.setWallet(wallet);

			var result = userService.canAfford(user, BigDecimal.valueOf(100));

			assertThat(result).isTrue();
		}

		@Test
		void testCanAffordFail() {
			User user = new User();
			Wallet wallet = new Wallet();
			wallet.setBalance(BigDecimal.valueOf(99));
			user.setWallet(wallet);

			var result = userService.canAfford(user, BigDecimal.valueOf(100));

			assertThat(result).isFalse();
		}
}
