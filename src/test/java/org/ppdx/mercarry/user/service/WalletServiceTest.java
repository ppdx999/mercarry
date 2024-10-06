package org.ppdx.mercarry.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ppdx.mercarry.core.BusinessException;
import org.ppdx.mercarry.user.domain.Wallet;
import org.ppdx.mercarry.user.repository.WalletRepository;

public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testChargeWallet() {
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);

        walletService.topUp(wallet, new BigDecimal("100"));

        ArgumentCaptor<Wallet> walletCaptor = ArgumentCaptor.forClass(Wallet.class);
        verify(walletRepository).save(walletCaptor.capture());

        Wallet capturedWallet = walletCaptor.getValue();
        assertThat(capturedWallet.getBalance()).isEqualTo(new BigDecimal("100"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1"})
    void testChargeWalletFaileWhenAmountIsNotPositive(String amount) {
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);

        assertThatThrownBy(() -> walletService.topUp(wallet, new BigDecimal(amount)))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Amount must be positive.");

        verify(walletRepository, never()).save(any());
    }

		@Test
		void testWithdrawWallet() {
				Wallet wallet = new Wallet();
				wallet.setBalance(new BigDecimal("100"));

				walletService.withdraw(wallet, new BigDecimal("50"));

				ArgumentCaptor<Wallet> walletCaptor = ArgumentCaptor.forClass(Wallet.class);
				verify(walletRepository).save(walletCaptor.capture());

				Wallet capturedWallet = walletCaptor.getValue();
				assertThat(capturedWallet.getBalance()).isEqualTo(new BigDecimal("50"));
		}

		@Test
		void testWithdrawWalletFailedWhenAmountIsNotPositive() {
				Wallet wallet = new Wallet();
				wallet.setBalance(new BigDecimal("100"));

				assertThatThrownBy(() -> walletService.withdraw(wallet, new BigDecimal("0")))
						.isInstanceOf(BusinessException.class)
						.hasMessage("Amount must be positive.");

				verify(walletRepository, never()).save(any());
		}

		@Test
		void testWithdrawWalletFailedWhenInsufficientBalance() {
				Wallet wallet = new Wallet();
				wallet.setBalance(new BigDecimal("100"));

				assertThatThrownBy(() -> walletService.withdraw(wallet, new BigDecimal("200")))
						.isInstanceOf(BusinessException.class)
						.hasMessage("Insufficient balance.");

				verify(walletRepository, never()).save(any());
		}

		@Test
		void testTransfer() {
				Wallet fromWallet = new Wallet();
				fromWallet.setBalance(new BigDecimal("100"));

				Wallet toWallet = new Wallet();
				toWallet.setBalance(new BigDecimal("50"));

				walletService.transfer(fromWallet, toWallet, new BigDecimal("10"));

				ArgumentCaptor<Wallet> walletCaptor = ArgumentCaptor.forClass(Wallet.class);
				verify(walletRepository, times(2)).save(walletCaptor.capture());

				Wallet capturedFromWallet = walletCaptor.getAllValues().get(0);
				Wallet capturedToWallet = walletCaptor.getAllValues().get(1);

				assertThat(capturedFromWallet.getBalance()).isEqualTo(new BigDecimal("90"));
				assertThat(capturedToWallet.getBalance()).isEqualTo(new BigDecimal("60"));
		}
}
