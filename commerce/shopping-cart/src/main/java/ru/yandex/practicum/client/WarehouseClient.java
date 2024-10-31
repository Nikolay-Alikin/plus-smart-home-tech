package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.generated.model.dto.BookedProductsDto;
import ru.yandex.practicum.generated.model.dto.ShoppingCartDto;

@FeignClient(name = "warehouse")
public interface WarehouseClient {

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/api/v1/warehouse/booking",
            produces = {"*/*"},
            consumes = {"application/json"}
    )
    BookedProductsDto bookingProductForShoppingCart(@RequestBody ShoppingCartDto shoppingCartDto);
}
