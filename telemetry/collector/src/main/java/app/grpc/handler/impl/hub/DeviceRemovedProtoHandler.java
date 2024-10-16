package app.grpc.handler.impl.hub;

import app.grpc.handler.HubProtoHandler;
import app.model.hub.event.impl.DeviceRemovedEvent;
import java.time.Instant;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto.PayloadCase;

@Component
public class DeviceRemovedProtoHandler implements HubProtoHandler<DeviceRemovedEvent> {

    @Override
    public PayloadCase getMessageType() {
        return PayloadCase.DEVICE_REMOVED;
    }

    @Override
    public DeviceRemovedEvent handle(HubEventProto hubEventProto) {
        DeviceRemovedEvent event = new DeviceRemovedEvent();
        DeviceRemovedEventProto eventProto = hubEventProto.getDeviceRemoved();

        event.setId(eventProto.getId());
        event.setHubId(hubEventProto.getHubId());
        event.setTimestamp(Instant.ofEpochMilli(hubEventProto.getTimestamp().getSeconds()));

        return event;
    }
}
