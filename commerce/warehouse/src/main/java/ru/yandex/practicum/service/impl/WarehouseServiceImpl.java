package ru.yandex.practicum.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.config.AddressProperties;
import ru.yandex.practicum.generated.model.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.generated.model.dto.AddressDto;
import ru.yandex.practicum.generated.model.dto.AssemblyProductForOrderFromShoppingCartRequest;
import ru.yandex.practicum.generated.model.dto.BookedProductsDto;
import ru.yandex.practicum.generated.model.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.generated.model.dto.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.generated.model.dto.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.generated.model.dto.ShoppingCartDto;
import ru.yandex.practicum.generated.model.dto.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.generated.model.dto.SpecifiedProductAlreadyInWarehouseException.HttpStatusEnum;
import ru.yandex.practicum.model.entity.BookingEntity;
import ru.yandex.practicum.model.entity.ProductEntity;
import ru.yandex.practicum.model.entity.jsonB.DimensionJson;
import ru.yandex.practicum.model.exception.LowQuantityInWarehouseException;
import ru.yandex.practicum.model.exception.NoProductInWarehouseException;
import ru.yandex.practicum.model.exception.NoSpecifiedProductException;
import ru.yandex.practicum.model.exception.ProductExistsException;
import ru.yandex.practicum.repository.BookingRepository;
import ru.yandex.practicum.repository.ProductRepository;
import ru.yandex.practicum.service.WarehouseService;

@Service
@Transactional
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final BookingRepository bookingRepository;
    private final ProductRepository productRepository;
    private final AddressProperties addressProperties;

    @Override
    public void acceptReturn(Map<String, Long> requestBody) {
        List<ProductEntity> entities = findAllByIds(requestBody);
        productRepository.saveAll(entities);
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseRequest addProductToWarehouseRequest) {
        var entity = getProductEntityById(addProductToWarehouseRequest.getProductId());
        entity.setQuantity(entity.getQuantity() + addProductToWarehouseRequest.getQuantity());
        productRepository.save(entity);
    }

    @Override
    public BookedProductsDto assemblyProductForOrderFromShoppingCart(
            AssemblyProductForOrderFromShoppingCartRequest assemblyProductForOrderFromShoppingCartRequest) {
        var shoppingCartId = assemblyProductForOrderFromShoppingCartRequest.getShoppingCartId();
        var bookings = bookingRepository.findAllByShoppingCartId(shoppingCartId).stream()
                .collect(Collectors.toMap(b -> b.getProductEntity().getProductId(), Function.identity()));

        var products = bookings.values().stream().map(BookingEntity::getProductEntity).toList();

        checkQuantity(products);

        List<ProductEntity> productsToSave = subtractQuantity(products, bookings);
        productRepository.saveAll(productsToSave);
        bookingRepository.deleteAllByShoppingCartId(shoppingCartId);
        return getBookedProductsDto(productsToSave);
    }

    @Override
    public BookedProductsDto bookingProductForShoppingCart(ShoppingCartDto shoppingCartDto) {
        var products = shoppingCartDto.getProducts();

        Predicate<ProductEntity> biggerOrEqual = entity ->
                entity.getQuantity() > products.get(entity.getProductId().toString())
                || entity.getQuantity() == products.get(entity.getProductId().toString());

        var entities = findAllByIds(products);

        var filteredEntities = entities
                .stream()
                .filter(biggerOrEqual)
                .collect(Collectors.toMap(ProductEntity::getProductId, Function.identity()));

        if (entities.size() != filteredEntities.size()) {
            var data = new ProductInShoppingCartLowQuantityInWarehouse();
            data.setHttpStatus(
                    ProductInShoppingCartLowQuantityInWarehouse.HttpStatusEnum._400_BAD_REQUEST);
            data.setMessage(
                    "Ошибка, товар из корзины не находится в требуемом количестве на складе"
            );
            throw new LowQuantityInWarehouseException(data);
        }

        var bookingEntities = filteredEntities.entrySet().stream()
                .map(e -> {
                    var bookingEntity = new BookingEntity();
                    bookingEntity.setShoppingCartId(shoppingCartDto.getShoppingCartId());
                    bookingEntity.setProductEntity(e.getValue());
                    bookingEntity.setQuantity(products.get(e.getKey().toString()));
                    return bookingEntity;
                }).toList();

        productRepository.saveAll(filteredEntities.values());
        bookingRepository.saveAll(bookingEntities);

        return getBookedProductsDto(filteredEntities.values());
    }

    @NotNull
    private static BookedProductsDto getBookedProductsDto(Collection<ProductEntity> filteredEntities) {
        var dto = new BookedProductsDto();
        dto.setFragile(filteredEntities.stream().anyMatch(ProductEntity::isFragile));
        dto.setDeliveryVolume(filteredEntities.stream().mapToDouble(ProductEntity::getWeight).sum());
        dto.setDeliveryWeight(filteredEntities.stream().mapToDouble(ProductEntity::getWeight).sum());
        return dto;
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return new AddressDto()
                .country(addressProperties.getCountry())
                .city(addressProperties.getCity())
                .street(addressProperties.getStreet())
                .house(addressProperties.getHouse())
                .flat(addressProperties.getFlat());
    }

    @Override
    public void newProductInWarehouse(NewProductInWarehouseRequest newProductInWarehouseRequest) {
        checkingProductExistence(newProductInWarehouseRequest.getProductId());
        var json = new DimensionJson();
        json.setWidth(newProductInWarehouseRequest.getDimension().getWidth());
        json.setHeight(newProductInWarehouseRequest.getDimension().getHeight());
        json.setDepth(newProductInWarehouseRequest.getDimension().getDepth());
        productRepository.save(new ProductEntity()
                .setProductId(newProductInWarehouseRequest.getProductId())
                .setWeight(newProductInWarehouseRequest.getWeight())
                .setDimension(json)
                .setFragile(newProductInWarehouseRequest.getFragile()));
    }

    private void checkingProductExistence(UUID productId) {
        productRepository.findById(productId).ifPresent((entity) -> {
            var data = new SpecifiedProductAlreadyInWarehouseException();
            data.setHttpStatus(HttpStatusEnum._400_BAD_REQUEST);
            data.setMessage("Ошибка, товар с таким описанием уже зарегистрирован на складе");
            throw new ProductExistsException(data);
        });
    }

    private ProductEntity getProductEntityById(UUID productId) {
        return productRepository.findById(productId).orElseThrow(() -> {
            var data = new NoSpecifiedProductInWarehouseException();
            data.setMessage("Product not found");
            data.setHttpStatus(NoSpecifiedProductInWarehouseException.HttpStatusEnum._400_BAD_REQUEST);
            return new NoSpecifiedProductException(data);
        });
    }

    private List<ProductEntity> findAllByIds(Map<String, Long> products) {
        List<UUID> ids = products.keySet().stream().map(UUID::fromString).toList();
        return productRepository.findAllById(ids)
                .stream()
                .map(entity -> entity.setQuantity(
                        entity.getQuantity() + products.get(entity.getProductId().toString())))
                .toList();
    }

    private static void checkQuantity(List<ProductEntity> products) {
        products.stream().filter(p -> p.getQuantity() <= 0).findAny().ifPresent(p -> {
            var data = new NoSpecifiedProductInWarehouseException();
            data.setHttpStatus(NoSpecifiedProductInWarehouseException.HttpStatusEnum._400_BAD_REQUEST);
            data.setMessage("Ошибка, товара из бронирования нет в БД склада");
            throw new NoProductInWarehouseException(data);
        });
    }

    private List<ProductEntity> subtractQuantity(List<ProductEntity> products, Map<UUID, BookingEntity> bookings) {
        return products.stream()
                .peek(p -> {
                    var quantity = bookings.get(p.getProductId()).getQuantity();
                    p.setQuantity(p.getQuantity() - quantity);
                })
                .toList();
    }
}
