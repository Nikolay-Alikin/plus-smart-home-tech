package ru.yandex.practicum.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.generated.model.dto.ProductNotFoundException;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundExceptionDto extends RuntimeException {

    private final ProductNotFoundException exception;

    public ProductNotFoundExceptionDto(ProductNotFoundException e) {
        exception = e;
    }
}
