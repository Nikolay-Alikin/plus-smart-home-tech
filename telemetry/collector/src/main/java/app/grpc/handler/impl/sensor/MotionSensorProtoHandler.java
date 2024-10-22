package app.grpc.handler.impl.sensor;

import app.grpc.handler.SensorProtoHandler;
import app.model.sensor.event.impl.MotionSensorEvent;
import java.time.Instant;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto.PayloadCase;

@Component
public class MotionSensorProtoHandler implements SensorProtoHandler<MotionSensorEvent> {

    @Override
    public PayloadCase getMessageType() {
        return PayloadCase.MOTION_SENSOR_EVENT;
    }

    @Override
    public MotionSensorEvent handle(SensorEventProto sensorEventProto) {
        MotionSensorEvent event = new MotionSensorEvent();
        MotionSensorProto eventProto = sensorEventProto.getMotionSensorEvent();

        event.setId(sensorEventProto.getId());
        event.setHubId(sensorEventProto.getHubId());
        event.setTimestamp(Instant.ofEpochSecond(sensorEventProto.getTimestamp().getSeconds()));
        event.setLinkQuality(eventProto.getLinkQuality());
        event.setMotion(eventProto.getMotion());
        event.setVoltage(eventProto.getVoltage());

        return event;
    }
}
