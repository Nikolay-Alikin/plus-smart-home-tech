package app.grpc.handler.impl.sensor;

import app.grpc.handler.SensorProtoHandler;
import app.model.sensor.event.impl.LightSensorEvent;
import java.time.Instant;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto.PayloadCase;

@Component
public class LightSensorProtoHandler implements SensorProtoHandler<LightSensorEvent> {

    @Override
    public PayloadCase getMessageType() {
        return PayloadCase.LIGHT_SENSOR_EVENT;
    }

    @Override
    public LightSensorEvent handle(SensorEventProto sensorEventProto) {
        LightSensorEvent event = new LightSensorEvent();
        LightSensorProto eventProto = sensorEventProto.getLightSensorEvent();

        event.setId(sensorEventProto.getId());
        event.setHubId(sensorEventProto.getHubId());
        event.setTimestamp(Instant.ofEpochSecond(sensorEventProto.getTimestamp().getSeconds()));
        event.setLuminosity(eventProto.getLuminosity());
        event.setLinkQuality(eventProto.getLinkQuality());

        return event;
    }
}
