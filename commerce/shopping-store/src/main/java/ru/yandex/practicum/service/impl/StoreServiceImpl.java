package ru.yandex.practicum.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.generated.model.dto.Pageable;
import ru.yandex.practicum.generated.model.dto.ProductDto;
import ru.yandex.practicum.generated.model.dto.ProductNotFoundException;
import ru.yandex.practicum.generated.model.dto.ProductNotFoundException.HttpStatusEnum;
import ru.yandex.practicum.generated.model.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.entity.ProductEntity;
import ru.yandex.practicum.model.exception.ProductNotFoundExceptionDto;
import ru.yandex.practicum.repository.ProductRepository;
import ru.yandex.practicum.service.StoreService;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final ProductMapper mapper;
    private final ProductRepository repository;

    @Override
    public ProductDto createNewProduct(ProductDto productDto) {
        ProductEntity entity;
        if (productDto.getProductId().isPresent()) {
            entity = findById(productDto.getProductId().get());
            mapper.merge(entity, productDto);
            ProductEntity savedEntity = repository.save(entity);
            return mapper.toDto(savedEntity);
        }
        entity = new ProductEntity();
        ProductEntity savedEntity = repository.save(entity);
        return mapper.toDto(savedEntity);
    }

    @Override
    public ProductDto getProduct(UUID productId) {
        return mapper.toDto(findById(productId));
    }

    @Override
    public List<ProductDto> getProducts(String category, Pageable pageable) {
        String[] sort = pageable.getSort().toArray(new String[0]);
        org.springframework.data.domain.Pageable pageRequest = PageRequest.of(
                pageable.getPage(),
                pageable.getSize(),
                Direction.DESC,
                sort);

        return mapper.toDto(repository.findAllByProductCategory(category, pageRequest));
    }

    @Override
    public Boolean removeProductFromStore(UUID productId) {
        if (!repository.existsById(productId)) {
            return false;
        }
        repository.deleteById(productId);
        return true;
    }

    @Override
    public Boolean setProductQuantityState(SetProductQuantityStateRequest setProductQuantityStateRequest) {
        ProductEntity entity = findById(setProductQuantityStateRequest.getProductId());
        entity.quantityState(setProductQuantityStateRequest.getQuantityState().name());
        repository.save(entity);
        return true;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        UUID productId = Optional.ofNullable(productDto.getProductId().get()).orElseThrow(() -> {
            ProductNotFoundException exception = new ProductNotFoundException();
            exception.setMessage("Product not found");
            exception.setHttpStatus(HttpStatusEnum._404_NOT_FOUND);
            return new ProductNotFoundExceptionDto(exception);
        });
        ProductEntity entity = findById(productId);
        mapper.merge(entity, productDto);
        repository.save(entity);
        return mapper.toDto(entity);
    }

    private ProductEntity findById(UUID productId) {
        return repository.findById(productId).orElseThrow(() -> {
            ProductNotFoundException exception = new ProductNotFoundException();
            exception.setMessage("Product not found");
            exception.setHttpStatus(HttpStatusEnum._404_NOT_FOUND);
            return new ProductNotFoundExceptionDto(exception);
        });
    }
}
