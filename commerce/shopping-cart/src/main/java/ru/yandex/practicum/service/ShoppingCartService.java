package ru.yandex.practicum.service;

import java.util.Map;
import ru.yandex.practicum.generated.model.dto.BookedProductsDto;
import ru.yandex.practicum.generated.model.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.generated.model.dto.ProductDto;
import ru.yandex.practicum.generated.model.dto.ShoppingCartDto;

public interface ShoppingCartService {

    ShoppingCartDto addProductToShoppingCart(String username, Map<String, Long> requestBody);

    BookedProductsDto bookingProductsFromShoppingCart(String username);

    ProductDto changeProductQuantity(String username, ChangeProductQuantityRequest changeProductQuantityRequest);

    void deactivateCurrentShoppingCart(String username);

    ShoppingCartDto getShoppingCart(String username);

    ShoppingCartDto removeFromShoppingCart(String username, Map<String, Long> requestBody);
}
