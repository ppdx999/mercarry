package org.ppdx.mercarry.user.service;

import org.ppdx.mercarry.user.domain.User;
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

    public Wallet create(User user) {
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setUser(user);
        return walletRepository.save(wallet);
    }

    public void topUp(Wallet wallet, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Amount must be positive.");
        }
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
    }

		public void withdraw(Wallet wallet, BigDecimal amount) {
				if (amount.compareTo(BigDecimal.ZERO) <= 0) {
						throw new BusinessException("Amount must be positive.");
				}
				if (wallet.getBalance().compareTo(amount) < 0) {
						throw new BusinessException("Insufficient balance.");
				}
				wallet.setBalance(wallet.getBalance().subtract(amount));
				walletRepository.save(wallet);
		}
}
