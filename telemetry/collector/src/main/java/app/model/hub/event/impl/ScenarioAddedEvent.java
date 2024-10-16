package app.model.hub.event.impl;

import app.model.hub.enumerated.HubEventType;
import app.model.hub.event.HubEvent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioAddedEvent extends HubEvent {

    @Min(3)
    @NotBlank
    @Max(Integer.MAX_VALUE)
    private String name;
    @NotEmpty
    private List<ScenarioCondition> conditions;
    @NotEmpty
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }

    public record ScenarioCondition(
            String sensorId,
            Type type,
            Operation operation,
            int value
    ) {

        public enum Type {
            MOTION,
            SWITCH,
            HUMIDITY,
            CO2LEVEL,
            LUMINOSITY,
            TEMPERATURE,
        }

        public enum Operation {
            EQUALS,
            LOWER_THAN,
            GREATER_THAN
        }
    }

    public record DeviceAction(
            String sensorId,
            Type type,
            String value
    ) {

        public enum Type {
            INVERSE,
            ACTIVATE,
            SET_VALUE,
            DEACTIVATE
        }
    }
}
