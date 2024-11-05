package ru.yandex.practicum.service;

import java.util.List;
import ru.yandex.practicum.generated.model.dto.AddProductToShoppingCartRequestInner;
import ru.yandex.practicum.generated.model.dto.BookedProductsDto;
import ru.yandex.practicum.generated.model.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.generated.model.dto.ProductDto;
import ru.yandex.practicum.generated.model.dto.ShoppingCartDto;

public interface ShoppingCartService {

    ShoppingCartDto addProductToShoppingCart(String username, List<AddProductToShoppingCartRequestInner> requestBody);

    BookedProductsDto bookingProductsFromShoppingCart(String username);

    ProductDto changeProductQuantity(String username, ChangeProductQuantityRequest changeProductQuantityRequest);

    void deactivateCurrentShoppingCart(String username);

    ShoppingCartDto getShoppingCart(String username);

    ShoppingCartDto removeFromShoppingCart(String username, List<AddProductToShoppingCartRequestInner> requestBody);
}
