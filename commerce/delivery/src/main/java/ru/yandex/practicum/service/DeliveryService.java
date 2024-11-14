package ru.yandex.practicum.service;

import java.math.BigDecimal;
import java.util.UUID;
import ru.yandex.practicum.generated.model.dto.DeliveryDto;
import ru.yandex.practicum.generated.model.dto.OrderDto;

public interface DeliveryService {

    BigDecimal deliveryCost(OrderDto orderDto);

    void deliveryFailed(UUID body);

    void deliveryPicked(UUID body);

    void deliverySuccessful(UUID body);

    DeliveryDto planDelivery(DeliveryDto deliveryDto);
}
