package com.PSI.OrderService.DTO;

import lombok.Data;

@Data
public class MakePaymentDTO {
    private Long orderId;
    private String paymentType;
}
