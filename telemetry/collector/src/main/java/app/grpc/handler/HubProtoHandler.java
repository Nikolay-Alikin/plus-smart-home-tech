package app.grpc.handler;

import app.model.hub.event.HubEvent;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface HubProtoHandler<R extends HubEvent> {

    HubEventProto.PayloadCase getMessageType();

    R handle(HubEventProto hubEventProto);
}
