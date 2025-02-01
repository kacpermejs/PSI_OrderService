package com.PSI.OrderService.Services;

import com.PSI.OrderService.Config.RestTemplate.RestTemplateErrorHandler;
import com.PSI.OrderService.DTO.MakePaymentResponseDTO;
import com.PSI.OrderService.Enums.OrderState;
import com.PSI.OrderService.Enums.PaymentType;
import com.PSI.OrderService.Model.Order;
import com.PSI.OrderService.Model.Payment;
import com.PSI.OrderService.Repositories.OrderRepository;
import com.PSI.OrderService.Repositories.PaymentRepository;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class PaymentService {

    PaymentRepository paymentRepository;
    OrderRepository orderRepository;
    private RestTemplate restTemplate;

    public PaymentService(PaymentRepository paymentRepository,
                          OrderRepository orderRepository, RestTemplateBuilder restTemplateBuilder) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateErrorHandler())
                .build();
    }

    // Klikniecie platnosci na stronie(Online lub Onsite)
    public MakePaymentResponseDTO makePayment(Long orderId, String paymentType) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        Payment payment = order.getPayment();

        if(payment == null) {
            payment = new Payment();
            paymentRepository.save(payment);
        }

        order.setPayment(payment);
        order.setState(OrderState.Paid);

        // Jesli platnosc online, to wstawiamy date, a jak onsite to wstawimy ja dopiero do platnosci przy kasie
        if(Objects.equals(paymentType, "Online")){
            payment.setPurchaseDate(LocalDateTime.now());
        }
        payment.setPaymentType(PaymentType.valueOf(paymentType));
        payment.setPaid(true);
        payment.setOrder(order);

        paymentRepository.save(payment);

        String url = UriComponentsBuilder.fromUriString("http://ticket-service:8082/tickets/order/pay/{orderId}")
                .buildAndExpand(orderId)
                .toUriString();

        // Wywołanie endpointu
        restTemplate.postForObject(url, null, Void.class);

        MakePaymentResponseDTO makePaymentResponseDTO = new MakePaymentResponseDTO();
        makePaymentResponseDTO.setOrderId(orderId);
        makePaymentResponseDTO.setOrderNumber(order.getOrderNumber());
        makePaymentResponseDTO.setState(order.getState());
        makePaymentResponseDTO.setFinalPrice(order.getFinalPrice());


        return makePaymentResponseDTO;
    }

    //Platnosc kasjerowi
    public Payment onsitePayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        Payment payment = order.getPayment();

        if(payment == null) {
            payment = new Payment();
        }

        payment.setPurchaseDate(LocalDateTime.now());
        payment.setPaymentType(PaymentType.Onsite);
        payment.setPaid(true);


        paymentRepository.save(payment);

        return payment;
    }
}
