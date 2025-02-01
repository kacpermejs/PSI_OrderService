package com.PSI.OrderService.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDTO {
    private List<TicketDTO> tickets;
    private String username;
}
