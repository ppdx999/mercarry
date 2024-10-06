package org.ppdx.mercarry.order.repository;

import org.ppdx.mercarry.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

