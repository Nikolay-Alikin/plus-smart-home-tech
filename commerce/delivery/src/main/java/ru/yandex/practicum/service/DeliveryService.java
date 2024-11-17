package ru.yandex.practicum.service;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {

    BigDecimal deliveryCost(ru.yandex.practicum.generated.model.delivery.dto.OrderDto orderDto);

    void deliveryFailed(UUID body);

    void deliveryPicked(UUID body);

    void deliverySuccessful(UUID body);

    ru.yandex.practicum.generated.model.delivery.dto.DeliveryDto planDelivery(
            ru.yandex.practicum.generated.model.delivery.dto.DeliveryDto deliveryDto);
}
