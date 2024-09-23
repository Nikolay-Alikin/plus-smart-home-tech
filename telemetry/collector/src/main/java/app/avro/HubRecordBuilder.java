package app.avro;

import app.model.hub.event.HubEvent;
import app.model.hub.event.impl.DeviceAddedEvent;
import app.model.hub.event.impl.DeviceRemovedEvent;
import app.model.hub.event.impl.ScenarioAddedEvent;
import app.model.hub.event.impl.ScenarioRemovedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubRecordBuilder implements RecordBuilder<HubEvent> {

    @Override
    public SpecificRecordBase buildSpecificRecordBase(HubEvent event) {
        HubEventAvro hubEventAvro = new HubEventAvro();
        hubEventAvro.setHubId(event.getHubId());
        hubEventAvro.setTimestamp(event.getTimestamp());

        Object payload = switch (event.getType()) {
            case DEVICE_ADDED -> toDeviceAddedEvent(event);
            case DEVICE_REMOVED -> toDeviceRemovedEvent(event);
            case SCENARIO_ADDED -> toScenarioAddedEvent(event);
            case SCENARIO_REMOVED -> toScenarioRemovedEvent(event);
        };
        hubEventAvro.setPayload(payload);

        return hubEventAvro;
    }


    private DeviceAddedEventAvro toDeviceAddedEvent(HubEvent event) {
        DeviceAddedEvent deviceAddedEvent;
        if (event instanceof DeviceAddedEvent) {
            deviceAddedEvent = (DeviceAddedEvent) event;
        } else {
            throw new ClassCastException("Невозможно привести событие к DeviceAddedEvent");
        }
        DeviceAddedEventAvro deviceAddedEventAvro = new DeviceAddedEventAvro();
        deviceAddedEventAvro.setId(deviceAddedEvent.getId());
        deviceAddedEventAvro.setType(DeviceTypeAvro.valueOf(deviceAddedEvent.getDeviceType()));
        return deviceAddedEventAvro;
    }

    private DeviceRemovedEventAvro toDeviceRemovedEvent(HubEvent event) {
        DeviceRemovedEvent deviceRemovedEvent;
        if (event instanceof DeviceRemovedEvent) {
            deviceRemovedEvent = (DeviceRemovedEvent) event;
        } else {
            throw new ClassCastException("Невозможно привести событие к DeviceRemovedEvent");
        }
        DeviceRemovedEventAvro deviceRemovedEventAvro = new DeviceRemovedEventAvro();
        deviceRemovedEventAvro.setId(deviceRemovedEvent.getId());
        return deviceRemovedEventAvro;
    }

    private ScenarioAddedEventAvro toScenarioAddedEvent(HubEvent event) {
        ScenarioAddedEvent scenarioAddedEvent;
        if (event instanceof ScenarioAddedEvent) {
            scenarioAddedEvent = (ScenarioAddedEvent) event;
        } else {
            throw new ClassCastException("Невозможно привести событие к ScenarioAddedEvent");
        }
        ScenarioAddedEventAvro scenarioAddedEventAvro = new ScenarioAddedEventAvro();
        scenarioAddedEventAvro.setName(scenarioAddedEvent.getName());
        scenarioAddedEventAvro.setConditions(scenarioAddedEvent.getConditions().stream()
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
        scenarioAddedEventAvro.setActions(scenarioAddedEvent.getActions().stream()
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

    private ScenarioRemovedEventAvro toScenarioRemovedEvent(HubEvent event) {
        ScenarioRemovedEvent scenarioRemovedEvent;
        if (event instanceof ScenarioRemovedEvent) {
            scenarioRemovedEvent = (ScenarioRemovedEvent) event;
        } else {
            throw new ClassCastException("Невозможно привести событие к ScenarioRemovedEvent");
        }
        ScenarioRemovedEventAvro scenarioRemovedEventAvro = new ScenarioRemovedEventAvro();
        scenarioRemovedEventAvro.setName(scenarioRemovedEvent.getName());
        return scenarioRemovedEventAvro;
    }
}
