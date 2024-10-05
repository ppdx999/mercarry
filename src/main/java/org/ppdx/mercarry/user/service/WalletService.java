package org.ppdx.mercarry.user.service;

import org.ppdx.mercarry.user.domain.Wallet;
import org.ppdx.mercarry.user.repository.WalletRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.ppdx.mercarry.core.BusinessException;

import java.math.BigDecimal;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    public void chargeWallet(Wallet wallet, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Amount must be positive.");
        }
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
    }
}