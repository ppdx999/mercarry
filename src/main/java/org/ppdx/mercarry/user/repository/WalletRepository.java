package org.ppdx.mercarry.user.repository;

import org.ppdx.mercarry.user.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
