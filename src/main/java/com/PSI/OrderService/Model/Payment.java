package com.PSI.OrderService.Model;

import com.PSI.OrderService.Enums.PaymentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime purchaseDate;

    private boolean paid;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @OneToOne(mappedBy = "payment")
    private Order order;
}
