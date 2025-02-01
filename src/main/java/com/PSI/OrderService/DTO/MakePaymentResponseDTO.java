package com.PSI.OrderService.DTO;

import com.PSI.OrderService.Enums.OrderState;
import com.PSI.OrderService.Model.Payment;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MakePaymentResponseDTO {

    // DTO for a response body from makepayment endpoint
    private Long orderId;

    private String orderNumber;

    private OrderState state;

    private Double finalPrice;

}
