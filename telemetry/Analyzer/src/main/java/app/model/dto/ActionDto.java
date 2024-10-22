package app.model.dto;

import lombok.Data;

@Data
public class ActionDto {

    private final Long SensorId;

    private final String type;

    private final Integer value;

}
