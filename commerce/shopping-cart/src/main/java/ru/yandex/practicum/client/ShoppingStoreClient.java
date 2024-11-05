package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.generated.model.dto.ProductDto;

@FeignClient(name = "shopping-store")
public interface ShoppingStoreClient {

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/v1/shopping-store/{productId}",
            produces = {"*/*"}
    )
    ProductDto getProduct(@PathVariable String productId);
}
