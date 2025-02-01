package com.PSI.OrderService.Repositories;

import com.PSI.OrderService.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
