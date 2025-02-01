package com.PSI.OrderService.DTO;

import lombok.Data;

import java.util.List;

@Data
public class OrderTicketDTO {
    private Long orderId;
    private List<Long> ticketIds;
}

