package ru.yandex.practicum.controller;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.generated.api.ApiApi;
import ru.yandex.practicum.generated.model.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.generated.model.dto.AddressDto;
import ru.yandex.practicum.generated.model.dto.AssemblyProductForOrderFromShoppingCartRequest;
import ru.yandex.practicum.generated.model.dto.BookedProductsDto;
import ru.yandex.practicum.generated.model.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.generated.model.dto.ShippedToDeliveryRequest;
import ru.yandex.practicum.generated.model.dto.ShoppingCartDto;
import ru.yandex.practicum.service.WarehouseService;

@RestController
@RequiredArgsConstructor
public class WarehouseController implements ApiApi {

    private final WarehouseService service;

    @Override
    public ResponseEntity<Void> acceptReturn(Map<String, Long> requestBody, List<Object> products) {
        service.acceptReturn(requestBody);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> addProductToWarehouse(AddProductToWarehouseRequest addProductToWarehouseRequest) {
        service.addProductToWarehouse(addProductToWarehouseRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<BookedProductsDto> assemblyProductForOrderFromShoppingCart(
            AssemblyProductForOrderFromShoppingCartRequest assemblyProductForOrderFromShoppingCartRequest) {
        var dto = service.assemblyProductForOrderFromShoppingCart(
                assemblyProductForOrderFromShoppingCartRequest);

        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<BookedProductsDto> bookingProductForShoppingCart(ShoppingCartDto shoppingCartDto) {
        var dto = service.bookingProductForShoppingCart(shoppingCartDto);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<AddressDto> getWarehouseAddress() {
        return ResponseEntity.ok(service.getWarehouseAddress());
    }

    @Override
    public ResponseEntity<Void> newProductInWarehouse(NewProductInWarehouseRequest newProductInWarehouseRequest2,
            NewProductInWarehouseRequest newProductInWarehouseRequest) {
        service.newProductInWarehouse(newProductInWarehouseRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> shippedToDelivery(ShippedToDeliveryRequest shippedToDeliveryRequest) {
        service.shippedToDelivery(shippedToDeliveryRequest);
        return ResponseEntity.ok().build();
    }
}
