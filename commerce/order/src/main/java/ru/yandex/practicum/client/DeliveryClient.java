package ru.yandex.practicum.client;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.generated.api.delivery.ApiDelivery;
import ru.yandex.practicum.generated.model.oder.dto.DeliveryDto;
import ru.yandex.practicum.generated.model.oder.dto.OrderDto;

@FeignClient(name = "delivery")
public interface DeliveryClient extends ApiDelivery {

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/api/v1/delivery/cost",
            produces = {"*/*"},
            consumes = {"application/json"}
    )
    ResponseEntity<BigDecimal> deliveryCost(
            @Parameter(name = "OrderDto", description = "", required = true) @Valid @RequestBody OrderDto orderDto
    );

    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/api/v1/delivery",
            produces = {"*/*"},
            consumes = {"application/json"}
    )
    ResponseEntity<DeliveryDto> planDelivery(
            @Parameter(name = "DeliveryDto", description = "", required = true) @Valid @RequestBody DeliveryDto deliveryDto
    );
}
