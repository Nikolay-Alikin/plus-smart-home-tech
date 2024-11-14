package ru.yandex.practicum.client;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.generated.model.dto.OrderDto;

@FeignClient(name = "payment")
public interface PaymentClient {

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/api/v1/payment/totalCost",
            produces = {"*/*"},
            consumes = {"application/json"}
    )
    BigDecimal getTotalCost(
            @Parameter(name = "OrderDto", required = true) @Valid @RequestBody OrderDto orderDto);
}
