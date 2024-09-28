package app.avro.handler.hub.impl;

import app.avro.handler.hub.HubEventHandler;
import app.model.hub.event.impl.ScenarioAddedEvent;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

public class ScenarioAddedEventHandler implements HubEventHandler<ScenarioAddedEvent, ScenarioAddedEventAvro> {

    @Override
    public ScenarioAddedEventAvro handleEvent(ScenarioAddedEvent event) {
        ScenarioAddedEventAvro scenarioAddedEventAvro = new ScenarioAddedEventAvro();
        scenarioAddedEventAvro.setName(event.getName());
        scenarioAddedEventAvro.setConditions(event.getConditions().stream()
                .map(condition -> {
                    ScenarioConditionAvro scenarioConditionAvro = new ScenarioConditionAvro();
                    scenarioConditionAvro.setType(ConditionTypeAvro.valueOf(String.valueOf(condition.type())));
                    scenarioConditionAvro.setOperation(
                            ConditionOperationAvro.valueOf(String.valueOf(condition.operation())));
                    scenarioConditionAvro.setSensorId(condition.sensorId());
                    scenarioConditionAvro.setValue(condition.value());
                    return scenarioConditionAvro;
                })
                .toList());
        scenarioAddedEventAvro.setActions(event.getActions().stream()
                .map(action -> {
                    DeviceActionAvro deviceActionAvro = new DeviceActionAvro();
                    deviceActionAvro.setType(ActionTypeAvro.valueOf(String.valueOf(action.type())));
                    deviceActionAvro.setSensorId(action.sensorId());
                    deviceActionAvro.setValue(Integer.valueOf(action.value()));
                    return deviceActionAvro;
                })
                .toList());
        return scenarioAddedEventAvro;
    }
}
