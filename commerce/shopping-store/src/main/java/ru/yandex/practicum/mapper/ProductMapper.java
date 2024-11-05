package ru.yandex.practicum.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.generated.model.dto.ProductCategory;
import ru.yandex.practicum.generated.model.dto.ProductDto;
import ru.yandex.practicum.generated.model.dto.ProductState;
import ru.yandex.practicum.generated.model.dto.QuantityState;
import ru.yandex.practicum.generated.model.dto.SetProductQuantityStateRequest.QuantityStateEnum;
import ru.yandex.practicum.model.entity.ProductEntity;

@Component
public class ProductMapper {

    public ProductEntity toDto(ProductDto dto) {
        return new ProductEntity()
                .id(dto.getProductId().orElse(null))
                .productName(dto.getProductName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .rating(dto.getRating())
                .quantityState(QuantityStateEnum.valueOf(dto.getQuantityState().name()))
                .productCategory(dto.getProductCategory())
                .imageSrc(dto.getImageSrc().orElse(null));
    }

    public ProductDto toDto(ProductEntity entity) {
        return new ProductDto()
                .productId(entity.id())
                .productName(entity.productName())
                .description(entity.description())
                .price(entity.price())
                .rating(entity.rating())
                .quantityState(QuantityState.valueOf(entity.quantityState().name()))
                .productState(ProductState.valueOf(entity.productState().name()))
                .productCategory(ProductCategory.valueOf(entity.productCategory().name()))
                .imageSrc(entity.imageSrc());
    }

    public List<ProductDto> toDto(List<ProductEntity> allByProductCategory) {
        return allByProductCategory.stream().map(this::toDto).toList();
    }

    public void merge(ProductEntity entity, ProductDto dto) {
        entity
                .productName(dto.getProductName() == null ? entity.productName() : dto.getProductName())
                .description(dto.getDescription() == null ? entity.description() : dto.getDescription())
                .price(dto.getPrice() == null ? entity.price() : dto.getPrice())
                .rating(dto.getRating() == null ? entity.rating() : dto.getRating())
                .quantityState(dto.getQuantityState() == null ? entity.quantityState()
                        : QuantityStateEnum.valueOf(dto.getQuantityState().name()))
                .productState(dto.getProductState() == null ? entity.productState() : dto.getProductState())
                .productCategory(
                        dto.getProductCategory() == null ? entity.productCategory() : dto.getProductCategory())
                .imageSrc(dto.getImageSrc() == null ? entity.imageSrc() : dto.getImageSrc().orElse(entity.imageSrc()));
    }
}
