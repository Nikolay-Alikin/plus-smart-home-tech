package ru.yandex.practicum.client;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.generated.model.dto.ShippedToDeliveryRequest;

@FeignClient(name = "warehouse")
public interface WarehouseClient {

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/api/v1/warehouse/shipped",
            consumes = {"application/json"}
    )
    void shippedToDelivery(
            @Parameter(name = "ShippedToDeliveryRequest", required = true)
            @Valid @RequestBody ShippedToDeliveryRequest shippedToDeliveryRequest);
}
