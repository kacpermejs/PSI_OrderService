package com.PSI.OrderService.Model;

import com.PSI.OrderService.Enums.OrderState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;

    private String username;

    private String orderNumber;

    @Enumerated(EnumType.STRING)
    private OrderState state;

    private Double finalPrice;

    private LocalDateTime createdAt;

    @PrePersist
    public void generateOrderNumber() {
        this.orderNumber = "ORD-" + LocalDate.now().getYear() + "-" + this.orderId;
    }
}
