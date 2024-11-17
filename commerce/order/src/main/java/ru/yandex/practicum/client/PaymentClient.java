package ru.yandex.practicum.client;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.generated.api.payment.ApiPayment;
import ru.yandex.practicum.generated.model.oder.dto.OrderDto;

@FeignClient(name = "payment")
public interface PaymentClient extends ApiPayment {

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/api/v1/payment/totalCost",
            produces = { "*/*" },
            consumes = { "application/json" }
    )
    ResponseEntity<BigDecimal> getTotalCost(
            @Parameter(name = "OrderDto", description = "", required = true) @Valid @RequestBody OrderDto orderDto
    );
}
