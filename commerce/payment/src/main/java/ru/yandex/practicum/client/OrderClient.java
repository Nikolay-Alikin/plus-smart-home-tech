package ru.yandex.practicum.client;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.generated.model.dto.OrderDto;

@FeignClient(name = "order")
public interface OrderClient {

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/api/v1/payment/failed",
            produces = {"*/*"},
            consumes = {"application/json"}
    )
    ResponseEntity<Void> paymentFailed(
            @Parameter(name = "body", required = true) @Valid @RequestBody UUID body);

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/api/v1/order/payment",
            produces = {"*/*"},
            consumes = {"application/json"}
    )
    ResponseEntity<OrderDto> payment(
            @Parameter(name = "body", required = true) @Valid @RequestBody UUID body
    );
}
