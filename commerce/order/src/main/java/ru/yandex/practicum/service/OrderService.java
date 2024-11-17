package ru.yandex.practicum.service;

import java.util.List;
import java.util.UUID;
import ru.yandex.practicum.generated.model.oder.dto.CreateNewOrderRequest;
import ru.yandex.practicum.generated.model.oder.dto.OrderDto;
import ru.yandex.practicum.generated.model.oder.dto.ProductReturnRequest;


public interface OrderService {

    OrderDto assembly(UUID body);

    OrderDto assemblyFailed(UUID body);

    OrderDto calculateDeliveryCost(UUID body);

    OrderDto calculateTotalCost(UUID body);

    OrderDto complete(UUID body);

    OrderDto createNewOrder(CreateNewOrderRequest createNewOrderRequest);

    OrderDto delivery(UUID body);

    OrderDto deliveryFailed(UUID body);

    List<OrderDto> getClientOrders(String username);

    OrderDto payment(UUID body);

    OrderDto paymentFailed(UUID body);

    OrderDto productReturn(ProductReturnRequest productReturnRequest);
}
