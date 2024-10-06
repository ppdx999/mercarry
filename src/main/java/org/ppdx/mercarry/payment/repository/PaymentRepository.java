package org.ppdx.mercarry.payment.repository;

import org.ppdx.mercarry.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}


