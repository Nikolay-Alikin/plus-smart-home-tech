package ru.yandex.practicum.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.client.ShoppingStoreClient;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.generated.model.dto.BookedProductsDto;
import ru.yandex.practicum.generated.model.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.generated.model.dto.NoProductsInShoppingCartException;
import ru.yandex.practicum.generated.model.dto.NoProductsInShoppingCartException.HttpStatusEnum;
import ru.yandex.practicum.generated.model.dto.ProductDto;
import ru.yandex.practicum.generated.model.dto.ShoppingCartDto;
import ru.yandex.practicum.model.entity.ProductEntity;
import ru.yandex.practicum.model.entity.ShoppingCartEntity;
import ru.yandex.practicum.model.exception.NoProductsInCartException;
import ru.yandex.practicum.repository.ProductRepository;
import ru.yandex.practicum.repository.ShoppingCartRepository;
import ru.yandex.practicum.service.ShoppingCartService;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final WarehouseClient warehouseClient;
    private final ShoppingStoreClient storeClient;
    private final ProductRepository productRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public ShoppingCartDto addProductToShoppingCart(String username, Map<String, Long> requestBody) {
        ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
        shoppingCartEntity.setUsername(username);
        ShoppingCartEntity createdEntity = shoppingCartRepository.save(shoppingCartEntity);

        List<ProductEntity> productEntities = buildProductEntities(requestBody, createdEntity);
        productRepository.saveAll(productEntities);

        return new ShoppingCartDto(createdEntity.getShoppingCartId(), buildProducts(productEntities));
    }

    @Override
    public BookedProductsDto bookingProductsFromShoppingCart(String username) {
        ShoppingCartEntity entity = shoppingCartRepository.findByUsername(username);
        ShoppingCartDto dto = new ShoppingCartDto(entity.getShoppingCartId(),
                buildProducts(new ArrayList<>(entity.getProductEntities())));

        return warehouseClient.bookingProductForShoppingCart(dto);
    }


    @Override
    public ProductDto changeProductQuantity(String username,
            ChangeProductQuantityRequest changeProductQuantityRequest) {
        ProductEntity productEntity = productRepository.findByIdAndShoppingCartEntityUsername(
                        changeProductQuantityRequest.getProductId(), username)
                .orElseThrow(() -> {
                    NoProductsInShoppingCartException e = new NoProductsInShoppingCartException();
                    e.setHttpStatus(HttpStatusEnum._400_BAD_REQUEST);
                    e.setMessage("Нет искомых товаров в корзине");
                    return new NoProductsInCartException(e);
                });
        productEntity.setCount(changeProductQuantityRequest.getNewQuantity());
        productRepository.save(productEntity);
        return storeClient.getProduct(productEntity.getId().toString());
    }

    @Override
    public void deactivateCurrentShoppingCart(String username) {
        shoppingCartRepository.deleteByUsername(username);
    }

    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        ShoppingCartEntity entity = shoppingCartRepository.findByUsername(username);
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();

        return new ShoppingCartDto(shoppingCartDto.getShoppingCartId(),
                buildProducts(new ArrayList<>(entity.getProductEntities())));
    }

    @Override
    public ShoppingCartDto removeFromShoppingCart(String username, Map<String, Long> requestBody) {
        ShoppingCartEntity entity = shoppingCartRepository.findByUsername(username);

        productRepository.deleteAll(entity.getProductEntities());

        List<ProductEntity> productEntities = buildProductEntities(requestBody, entity);

        entity.setProductEntities(new HashSet<>(productEntities));

        return null;
    }

    private List<ProductEntity> buildProductEntities(Map<String, Long> requestBody,
            ShoppingCartEntity shoppingCartEntity) {
        return requestBody.entrySet().stream()
                .map(entry -> {
                    ProductEntity productEntity = new ProductEntity();
                    productEntity.setId(UUID.fromString(entry.getKey()));
                    productEntity.setCount(entry.getValue());
                    productEntity.setShoppingCartEntity(shoppingCartEntity);
                    return productEntity;
                }).toList();
    }

    private Map<String, Long> buildProducts(List<ProductEntity> productEntities) {
        return productEntities.stream()
                .collect(Collectors.toMap(e -> e.getId().toString(), ProductEntity::getCount));
    }
}