package com.PSI.OrderService.Repositories;

import com.PSI.OrderService.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
