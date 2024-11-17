package ru.yandex.practicum.controller;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.generated.api.payment.ApiPayment;
import ru.yandex.practicum.generated.model.payment.dto.OrderDto;
import ru.yandex.practicum.generated.model.payment.dto.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

@RestController
@RequiredArgsConstructor
public class PaymentController implements ApiPayment {

    private final PaymentService service;

    @Override
    public ResponseEntity<BigDecimal> getTotalCost(OrderDto orderDto) {
        return ResponseEntity.ok(service.getTotalCost(orderDto));
    }

    @Override
    public ResponseEntity<PaymentDto> payment(OrderDto orderDto) {
        return ResponseEntity.ok(service.payment(orderDto));
    }

    @Override
    public ResponseEntity<Void> paymentFailed(UUID body) {
        service.paymentFailed(body);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> paymentSuccess(UUID body) {
        service.paymentSuccess(body);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<BigDecimal> productCost(OrderDto orderDto) {
        return ResponseEntity.ok(service.productCost(orderDto));
    }
}
