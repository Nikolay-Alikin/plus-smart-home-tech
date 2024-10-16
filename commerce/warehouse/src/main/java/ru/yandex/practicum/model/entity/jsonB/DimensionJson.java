package ru.yandex.practicum.model.entity.jsonB;

import java.io.Serializable;
import lombok.Data;

@Data
public class DimensionJson implements Serializable {

    private double width;
    private double height;
    private double depth;
}
