package com.PSI.OrderService.Controllers;

import com.PSI.OrderService.DTO.MakePaymentDTO;
import com.PSI.OrderService.DTO.MakePaymentResponseDTO;
import com.PSI.OrderService.Model.Order;
import com.PSI.OrderService.Model.Payment;
import com.PSI.OrderService.Services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/makepayment")
    public ResponseEntity<MakePaymentResponseDTO> makePayment(@RequestBody MakePaymentDTO payment) {
        return ResponseEntity.ok().body(paymentService.makePayment(payment.getOrderId(), payment.getPaymentType()));
    }

    @PatchMapping("/onsitepayment")
    public ResponseEntity<Payment> onSitePayment(@RequestBody Long orderId) {
        return ResponseEntity.ok().body(paymentService.onsitePayment(orderId));
    }

}
