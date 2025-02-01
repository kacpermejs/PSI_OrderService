package com.PSI.OrderService.Services;

import com.PSI.OrderService.Config.RestTemplate.RestTemplateErrorHandler;
import com.PSI.OrderService.DTO.OrderDetailsDTO;
import com.PSI.OrderService.DTO.OrderTicketDTO;
import com.PSI.OrderService.DTO.TicketDTO;
import com.PSI.OrderService.DTO.TicketDetailsDTO;
import com.PSI.OrderService.Enums.OrderState;
import com.PSI.OrderService.Model.Order;
import com.PSI.OrderService.Model.Payment;
import com.PSI.OrderService.Repositories.OrderRepository;
import com.PSI.OrderService.Repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final PaymentRepository paymentRepository;

    private RestTemplate restTemplate;

    private final RestClient restClient = RestClient.create();

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        RestTemplateBuilder restTemplateBuilder,
                        PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateErrorHandler())
                .build();
    }

    public Order createOrder(List<TicketDTO> tickets, String username) {

        Order order = new Order();
        order = orderRepository.save(order);

        System.out.println("Tickets from request body: " + tickets);

        if (tickets.isEmpty()) {
            System.out.println("Tickets from request body is empty");
            throw new RuntimeException("Tickets from request body is empty");
        }

        List<Long> ticketids = new ArrayList<>();

        for(TicketDTO ticketDTO : tickets) {
            ticketids.add(ticketDTO.getId());
        }

        System.out.println("TicketIds from request body: " + ticketids);
        OrderTicketDTO ticketDTO = new OrderTicketDTO();
        ticketDTO.setTicketIds(ticketids);
        ticketDTO.setOrderId(order.getOrderId());

        //Sprawdź, czy bilety są wolne
        Boolean result = restTemplate.postForObject("http://ticket-service:8082/tickets/order/check", ticketDTO, Boolean.class);

        // Wynik
        if (!result) {
            throw new IllegalStateException("Niektóre bilety są już zarezerwowane!");
        }

        Double finalPrice = 0.0;

        for (TicketDTO ticket : tickets) {
            finalPrice += ticket.getPrice();
        }

        order.setFinalPrice(finalPrice);

        order.setCreatedAt(LocalDateTime.now());
        order.setUsername(username);
        order.setState(OrderState.Created);

        orderRepository.save(order);

        // Tutaj jeszcze jeden strzał, żeby zapisać te bilety dla tego orderu

        return order;
    }


    public OrderDetailsDTO orderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        Payment payment = order.getPayment();

        if(payment == null) {
            throw new RuntimeException("Payment not found");
        }

        String url = String.format("http://ticket-service:8082/tickets/order/%d", orderId);

        List<TicketDetailsDTO> tickets = restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<List<TicketDetailsDTO>>() {});

        System.out.println("Details of tickets for order:" + tickets);
        assert tickets != null;
        TicketDetailsDTO firstTicket = tickets.get(0);
        System.out.println(firstTicket.getEventName());
        System.out.println(firstTicket.getEventStart().toString());
        System.out.println(firstTicket.getQrCode());


        OrderDetailsDTO orderDetails = new OrderDetailsDTO();

        orderDetails.setOrderId(orderId);
        orderDetails.setOrderNumber(order.getOrderNumber());
        orderDetails.setUsername(order.getUsername());
        orderDetails.setFinalPrice(order.getFinalPrice());
        orderDetails.setOrderState(String.valueOf(order.getState()));
        orderDetails.setFinalPrice(order.getFinalPrice());
        orderDetails.setCreatedAt(order.getCreatedAt());
        orderDetails.setPurchasedAt(payment.getPurchaseDate());
        orderDetails.setPaid(payment.isPaid());
        orderDetails.setPaymentType(String.valueOf(payment.getPaymentType()));
        orderDetails.setTickets(tickets);

        return orderDetails;
    }
}

