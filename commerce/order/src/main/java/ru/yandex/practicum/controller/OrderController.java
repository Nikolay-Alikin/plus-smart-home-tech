package ru.yandex.practicum.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.generated.api.ApiApi;
import ru.yandex.practicum.generated.model.dto.CreateNewOrderRequest;
import ru.yandex.practicum.generated.model.dto.OrderDto;
import ru.yandex.practicum.generated.model.dto.ProductReturnRequest;
import ru.yandex.practicum.service.OrderService;

@RestController
@RequiredArgsConstructor
public class OrderController implements ApiApi {

    private final OrderService orderService;

    @Override
    public ResponseEntity<OrderDto> assembly(UUID body) {
        return ResponseEntity.ok(orderService.assembly(body));
    }

    @Override
    public ResponseEntity<OrderDto> assemblyFailed(UUID body) {
        return ResponseEntity.ok(orderService.assemblyFailed(body));
    }

    @Override
    public ResponseEntity<OrderDto> calculateDeliveryCost(UUID body) {
        return ResponseEntity.ok(orderService.calculateDeliveryCost(body));
    }

    @Override
    public ResponseEntity<OrderDto> calculateTotalCost(UUID body) {
        return ResponseEntity.ok(orderService.calculateTotalCost(body));
    }

    @Override
    public ResponseEntity<OrderDto> complete(UUID body) {
        return ResponseEntity.ok(orderService.complete(body));
    }

    @Override
    public ResponseEntity<OrderDto> createNewOrder(CreateNewOrderRequest createNewOrderRequest) {
        return ResponseEntity.ok(orderService.createNewOrder(createNewOrderRequest));
    }

    @Override
    public ResponseEntity<OrderDto> delivery(UUID body) {
        return ResponseEntity.ok(orderService.delivery(body));
    }

    @Override
    public ResponseEntity<OrderDto> deliveryFailed(UUID body) {
        return ResponseEntity.ok(orderService.deliveryFailed(body));
    }

    @Override
    public ResponseEntity<List<OrderDto>> getClientOrders(String username) {
        return ResponseEntity.ok(orderService.getClientOrders(username));
    }

    @Override
    public ResponseEntity<OrderDto> payment(UUID body) {
        return ResponseEntity.ok(orderService.payment(body));
    }

    @Override
    public ResponseEntity<OrderDto> paymentFailed(UUID body) {
        return ResponseEntity.ok(orderService.paymentFailed(body));
    }

    @Override
    public ResponseEntity<OrderDto> productReturn(ProductReturnRequest productReturnRequest) {
        return ResponseEntity.ok(orderService.productReturn(productReturnRequest));
    }
}
