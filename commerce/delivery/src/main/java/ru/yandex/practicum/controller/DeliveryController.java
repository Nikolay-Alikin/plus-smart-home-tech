package ru.yandex.practicum.controller;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.generated.api.delivery.ApiDelivery;
import ru.yandex.practicum.generated.model.delivery.dto.DeliveryDto;
import ru.yandex.practicum.generated.model.delivery.dto.OrderDto;
import ru.yandex.practicum.service.DeliveryService;

@RestController
@RequiredArgsConstructor
public class DeliveryController implements ApiDelivery {

    private final DeliveryService service;

    @Override
    public ResponseEntity<BigDecimal> deliveryCost(OrderDto orderDto) {
        return ResponseEntity.ok(service.deliveryCost(orderDto));
    }

    @Override
    public ResponseEntity<Void> deliveryFailed(UUID body) {
        service.deliveryFailed(body);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deliveryPicked(UUID body) {
        service.deliveryPicked(body);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deliverySuccessful(UUID body) {
        service.deliverySuccessful(body);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<DeliveryDto> planDelivery(DeliveryDto deliveryDto) {
        return ResponseEntity.ok(service.planDelivery(deliveryDto));
    }
}
