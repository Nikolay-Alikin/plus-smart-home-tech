package ru.yandex.practicum.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.generated.model.dto.NoOrderFoundException;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    private final NoOrderFoundException data;

    public NotFoundException(NoOrderFoundException data) {
        this.data = data;
    }
}

