package app.grpc.handler.impl.hub;

import app.grpc.handler.HubProtoHandler;
import app.model.hub.event.impl.DeviceAddedEvent;
import java.time.Instant;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto.PayloadCase;

@Component
public class DeviceAddedProtoHandler implements HubProtoHandler<DeviceAddedEvent> {

    @Override
    public PayloadCase getMessageType() {
        return PayloadCase.DEVICE_ADDED;
    }

    @Override
    public DeviceAddedEvent handle(HubEventProto hubEventProto) {
        DeviceAddedEvent event = new DeviceAddedEvent();
        DeviceAddedEventProto eventProto = hubEventProto.getDeviceAdded();

        event.setId(eventProto.getId());
        event.setHubId(hubEventProto.getHubId());
        event.setTimestamp(Instant.ofEpochSecond(hubEventProto.getTimestamp().getSeconds()));
        event.setDeviceType(eventProto.getType().name());

        return event;
    }
}
