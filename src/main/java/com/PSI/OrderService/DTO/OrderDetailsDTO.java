package com.PSI.OrderService.DTO;

import com.PSI.OrderService.Enums.OrderState;
import com.PSI.OrderService.Model.Order;
import com.PSI.OrderService.Model.Payment;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailsDTO {
    private Long orderId;

    private String username;

    private String orderNumber;

    private String orderState;

    private Double finalPrice;

    private LocalDateTime createdAt;

    private LocalDateTime purchasedAt;

    private boolean paid;

    private String paymentType;


    private List<TicketDetailsDTO> tickets;
}
