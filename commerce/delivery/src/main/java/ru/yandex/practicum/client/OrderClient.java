package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.generated.api.order.ApiOrder;

@FeignClient(name = "order")
public interface OrderClient extends ApiOrder {

}
