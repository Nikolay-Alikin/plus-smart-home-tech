package app.grpc.handler.impl.sensor;

import app.grpc.handler.SensorProtoHandler;
import app.model.sensor.event.impl.SwitchSensorEvent;
import java.time.Instant;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto.PayloadCase;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;

@Component
public class SwitchSensorProtoHandler implements SensorProtoHandler<SwitchSensorEvent> {

    @Override
    public PayloadCase getMessageType() {
        return PayloadCase.SWITCH_SENSOR_EVENT;
    }

    @Override
    public SwitchSensorEvent handle(SensorEventProto sensorEventProto) {
        SwitchSensorEvent event = new SwitchSensorEvent();
        SwitchSensorProto eventProto = sensorEventProto.getSwitchSensorEvent();

        event.setId(sensorEventProto.getId());
        event.setHubId(sensorEventProto.getHubId());
        event.setTimestamp(Instant.ofEpochSecond(sensorEventProto.getTimestamp().getSeconds()));
        event.setState(eventProto.getState());

        return event;
    }
}
