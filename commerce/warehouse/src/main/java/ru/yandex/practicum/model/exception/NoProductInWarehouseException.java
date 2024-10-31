package ru.yandex.practicum.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.generated.model.dto.NoSpecifiedProductInWarehouseException;

@Getter
@Setter
@AllArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoProductInWarehouseException extends RuntimeException {

    private NoSpecifiedProductInWarehouseException data;
}
