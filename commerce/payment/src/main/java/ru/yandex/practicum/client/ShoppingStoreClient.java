package ru.yandex.practicum.client;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.generated.model.dto.ProductDto;

@FeignClient("shopping-store")
public interface ShoppingStoreClient {

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/v1/shopping-store/{productId}",
            produces = {"*/*"}
    )
    ResponseEntity<ProductDto> getProduct(
            @Parameter(name = "productId", description = "Идентификатор товара в БД.",
                    required = true, in = ParameterIn.PATH) @PathVariable("productId") UUID productId);
}
