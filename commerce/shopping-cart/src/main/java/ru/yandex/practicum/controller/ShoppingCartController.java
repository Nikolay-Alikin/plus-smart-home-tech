package ru.yandex.practicum.controller;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.generated.api.ApiApi;
import ru.yandex.practicum.generated.model.dto.BookedProductsDto;
import ru.yandex.practicum.generated.model.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.generated.model.dto.ProductDto;
import ru.yandex.practicum.generated.model.dto.ShoppingCartDto;
import ru.yandex.practicum.service.ShoppingCartService;

@RestController
@RequiredArgsConstructor
public class ShoppingCartController implements ApiApi {

    private final ShoppingCartService service;

    //TODO описание query больше подходит для body. По ощущениям, query здесь тоже не нужен.
    @Override
    public ResponseEntity<ShoppingCartDto> addProductToShoppingCart(String username, Map<String, Long> requestBody,
            List<Object> products) {
        return ResponseEntity.ok(service.addProductToShoppingCart(username, requestBody));
    }

    @Override
    public ResponseEntity<BookedProductsDto> bookingProductsFromShoppingCart(String username) {
        ResponseEntity.ok(service.bookingProductsFromShoppingCart(username));
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    //TODO снова дублирующийся объект в query и body
    @Override
    public ResponseEntity<ProductDto> changeProductQuantity(String username,
            ChangeProductQuantityRequest changeProductQuantityRequest2,
            ChangeProductQuantityRequest changeProductQuantityRequest) {
        ResponseEntity.ok(service.changeProductQuantity(username, changeProductQuantityRequest2));
        return null;
    }

    @Override
    public ResponseEntity<Void> deactivateCurrentShoppingCart(String username) {
        service.deactivateCurrentShoppingCart(username);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ShoppingCartDto> getShoppingCart(String username) {
        return ResponseEntity.ok(service.getShoppingCart(username));
    }

    //TODO описание query больше подходит для body. По ощущениям, query здесь тоже не нужен.
    @Override
    public ResponseEntity<ShoppingCartDto> removeFromShoppingCart(String username, Map<String, Long> requestBody,
            List<Object> products) {
        return ResponseEntity.ok(service.removeFromShoppingCart(username, requestBody));
    }
}
