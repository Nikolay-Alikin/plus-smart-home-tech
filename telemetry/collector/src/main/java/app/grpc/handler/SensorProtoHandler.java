package app.grpc.handler;

import app.model.sensor.event.SensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface SensorProtoHandler<R extends SensorEvent> {

    SensorEventProto.PayloadCase getMessageType();

    R handle(SensorEventProto sensorEventProto);

}
