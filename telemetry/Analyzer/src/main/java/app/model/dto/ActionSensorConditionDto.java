package app.model.dto;

import app.model.entity.Action;
import app.model.entity.Condition;
import app.model.entity.Sensor;

public record ActionSensorConditionDto(Condition condition,
                                       Sensor sensor,
                                       Action action) {

}
