package ru.yandex.practicum.client;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.generated.model.dto.AddressDto;
import ru.yandex.practicum.generated.model.dto.AssemblyProductForOrderFromShoppingCartRequest;
import ru.yandex.practicum.generated.model.dto.BookedProductsDto;

@FeignClient(name = "warehouse")
public interface WarehouseClient {

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/api/v1/warehouse/assembly",
            produces = {"*/*"},
            consumes = {"application/json"}
    )
    BookedProductsDto assemblyProductForOrderFromShoppingCart(
            @Parameter(name = "AssemblyProductForOrderFromShoppingCartRequest", required = true)
            @Valid @RequestBody AssemblyProductForOrderFromShoppingCartRequest assemblyProductForOrderFromShoppingCartRequest
    );

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/v1/warehouse/address",
            produces = {"*/*"}
    )
    AddressDto getWarehouseAddress();
}
