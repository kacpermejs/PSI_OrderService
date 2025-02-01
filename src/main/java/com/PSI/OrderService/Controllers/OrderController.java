package com.PSI.OrderService.Controllers;

import com.PSI.OrderService.DTO.CreateOrderDTO;
import com.PSI.OrderService.DTO.OrderDetailsDTO;
import com.PSI.OrderService.DTO.OrderTicketDTO;
import com.PSI.OrderService.DTO.TicketDTO;
import com.PSI.OrderService.Model.Order;
import com.PSI.OrderService.Services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        try{
            return ResponseEntity.ok().body(orderService.createOrder(createOrderDTO.getTickets(), createOrderDTO.getUsername()));
        }
        catch(IllegalStateException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/orderdetails/{id}")
    public ResponseEntity<OrderDetailsDTO> getOrderDetails(@PathVariable Long id) {
        return ResponseEntity.ok().body(orderService.orderDetails(id));
    }

}
