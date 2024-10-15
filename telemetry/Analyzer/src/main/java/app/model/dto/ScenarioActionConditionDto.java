package app.model.dto;

import app.model.entity.Action;
import app.model.entity.Condition;
import app.model.entity.Scenario;
import app.model.entity.Sensor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScenarioActionConditionDto {

    private Sensor sensor;
    private Action action;
    private Scenario scenario;
    private Condition condition;
}
