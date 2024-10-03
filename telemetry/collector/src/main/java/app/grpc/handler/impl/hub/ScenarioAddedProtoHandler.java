package app.grpc.handler.impl.hub;

import app.grpc.handler.HubProtoHandler;
import app.model.hub.event.impl.ScenarioAddedEvent;
import app.model.hub.event.impl.ScenarioAddedEvent.DeviceAction;
import app.model.hub.event.impl.ScenarioAddedEvent.ScenarioCondition;
import app.model.hub.event.impl.ScenarioAddedEvent.ScenarioCondition.Operation;
import app.model.hub.event.impl.ScenarioAddedEvent.ScenarioCondition.Type;
import java.time.Instant;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto.PayloadCase;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;

@Component
public class ScenarioAddedProtoHandler implements HubProtoHandler<ScenarioAddedEvent> {

    @Override
    public PayloadCase getMessageType() {
        return PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public ScenarioAddedEvent handle(HubEventProto hubEventProto) {
        ScenarioAddedEvent event = new ScenarioAddedEvent();
        ScenarioAddedEventProto eventProto = hubEventProto.getScenarioAdded();

        event.setHubId(hubEventProto.getHubId());
        event.setTimestamp(Instant.ofEpochSecond(hubEventProto.getTimestamp().getSeconds()));
        event.setName(eventProto.getName());
        event.setActions(eventProto.getActionList().stream()
                .map(actionProto -> new DeviceAction(actionProto.getSensorId(),
                        DeviceAction.Type.valueOf(actionProto.getType().name()),
                        String.valueOf(actionProto.getValue())))
                .toList());
        event.setConditions(eventProto.getConditionList().stream()
                .map(conditionProto ->
                {
                    int value;
                    switch (conditionProto.getValueCase()) {
                        case INT_VALUE -> value = conditionProto.getIntValue();
                        case BOOL_VALUE -> value = conditionProto.getBoolValue() ? 1 : 0;
                        default -> throw new IllegalArgumentException("Неизвестный тип значения");
                    }
                    return new ScenarioCondition(conditionProto.getSensorId(),
                            Type.valueOf(conditionProto.getType().name()),
                            Operation.valueOf(conditionProto.getOperation().name()),
                            value);
                })
                .toList());
        return event;
    }
}
