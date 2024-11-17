package ru.yandex.practicum.service;

import java.util.Map;
import ru.yandex.practicum.generated.model.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.generated.model.dto.AddressDto;
import ru.yandex.practicum.generated.model.dto.AssemblyProductForOrderFromShoppingCartRequest;
import ru.yandex.practicum.generated.model.dto.BookedProductsDto;
import ru.yandex.practicum.generated.model.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.generated.model.dto.ShippedToDeliveryRequest;
import ru.yandex.practicum.generated.model.dto.ShoppingCartDto;

public interface WarehouseService {

    void acceptReturn(Map<String, Long> requestBody);

    void addProductToWarehouse(AddProductToWarehouseRequest addProductToWarehouseRequest);

    BookedProductsDto assemblyProductForOrderFromShoppingCart(
            AssemblyProductForOrderFromShoppingCartRequest assemblyProductForOrderFromShoppingCartRequest);

    BookedProductsDto bookingProductForShoppingCart(ShoppingCartDto shoppingCartDto);

    AddressDto getWarehouseAddress();

    void newProductInWarehouse(NewProductInWarehouseRequest newProductInWarehouseRequest);

    void shippedToDelivery(ShippedToDeliveryRequest shippedToDeliveryRequest);
}
