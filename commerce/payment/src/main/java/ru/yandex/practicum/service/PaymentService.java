package ru.yandex.practicum.service;

import java.math.BigDecimal;
import java.util.UUID;
import ru.yandex.practicum.generated.model.dto.OrderDto;
import ru.yandex.practicum.generated.model.dto.PaymentDto;

public interface PaymentService {

    BigDecimal getTotalCost(OrderDto orderDto);

    PaymentDto payment(OrderDto orderDto);

    void paymentFailed(UUID body);

    void paymentSuccess(UUID body);

    BigDecimal productCost(OrderDto orderDto);
}
