package ru.yandex.practicum.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.generated.model.oder.dto.NoOrderFoundException;

@Data
@EqualsAndHashCode(callSuper = true)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoOrderFound extends RuntimeException {

    private final NoOrderFoundException data;
}

