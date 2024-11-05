package ru.yandex.practicum.service;

import java.util.List;
import java.util.UUID;
import ru.yandex.practicum.generated.model.dto.Pageable;
import ru.yandex.practicum.generated.model.dto.ProductDto;
import ru.yandex.practicum.generated.model.dto.SetProductQuantityStateRequest;

public interface StoreService {

    ProductDto createNewProduct(ProductDto productDto);

    ProductDto getProduct(UUID productId);

    List<ProductDto> getProducts(String category, Pageable pageable);

    Boolean removeProductFromStore(UUID productId);

    Boolean setProductQuantityState(SetProductQuantityStateRequest setProductQuantityStateRequest);

    ProductDto updateProduct(ProductDto productDto);
}
