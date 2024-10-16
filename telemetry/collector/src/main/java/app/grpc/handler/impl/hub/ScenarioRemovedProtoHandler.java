package app.grpc.handler.impl.hub;

import app.grpc.handler.HubProtoHandler;
import app.model.hub.event.impl.ScenarioRemovedEvent;
import java.time.Instant;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto.PayloadCase;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;

@Component
public class ScenarioRemovedProtoHandler implements HubProtoHandler<ScenarioRemovedEvent> {

    @Override
    public PayloadCase getMessageType() {
        return PayloadCase.SCENARIO_REMOVED;
    }

    @Override
    public ScenarioRemovedEvent handle(HubEventProto hubEventProto) {
        ScenarioRemovedEvent event = new ScenarioRemovedEvent();
        ScenarioRemovedEventProto eventProto = hubEventProto.getScenarioRemoved();

        event.setHubId(hubEventProto.getHubId());
        event.setTimestamp(Instant.ofEpochSecond(hubEventProto.getTimestamp().getSeconds()));
        event.setName(eventProto.getName());

        return null;
    }
}
