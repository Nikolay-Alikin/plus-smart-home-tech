package ru.yandex.practicum.controller;


import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.generated.api.ApiApi;
import ru.yandex.practicum.generated.model.dto.Pageable;
import ru.yandex.practicum.generated.model.dto.ProductDto;
import ru.yandex.practicum.generated.model.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.service.StoreService;

@RestController
@RequiredArgsConstructor
public class StoreController implements ApiApi {

    private final StoreService service;

    //TODO зачем в query и в body передавать один и тот же объект? Ошибка в спецификации?
    @Override
    public ResponseEntity<ProductDto> createNewProduct(ProductDto productDto, ProductDto product) {
        //product игнорируем
        return ResponseEntity.ok(service.createNewProduct(productDto));
    }

    @Override
    public ResponseEntity<ProductDto> getProduct(UUID productId) {
        return ResponseEntity.ok(service.getProduct(productId));
    }

    @Override
    public ResponseEntity<List<ProductDto>> getProducts(String category, Pageable pageable) {
        return ResponseEntity.ok(service.getProducts(category, pageable));
    }

    //TODO зачем в path и в body передавать один и тот же объект? Ошибка в спецификации?
    @Override
    public ResponseEntity<Boolean> removeProductFromStore(UUID body, UUID productId) {
        //body игнорируем
        return ResponseEntity.ok(service.removeProductFromStore(productId));
    }

    @Override
    public ResponseEntity<Boolean> setProductQuantityState(
            SetProductQuantityStateRequest setProductQuantityStateRequest) {
        ResponseEntity.ok(service.setProductQuantityState(setProductQuantityStateRequest));
        return null;
    }

    //TODO зачем в query и в body передавать один и тот же объект? Ошибка в спецификации?
    @Override
    public ResponseEntity<ProductDto> updateProduct(ProductDto productDto, ProductDto product) {
        return ResponseEntity.ok(service.updateProduct(productDto));
    }
}
