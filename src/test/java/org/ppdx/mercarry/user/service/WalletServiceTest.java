package org.ppdx.mercarry.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
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

        walletService.chargeWallet(wallet, new BigDecimal("100"));

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

        assertThatThrownBy(() -> walletService.chargeWallet(wallet, new BigDecimal(amount)))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Amount must be positive.");

        verify(walletRepository, never()).save(any());
    }
}
