package app.grpc.handler.impl.sensor;

import app.grpc.handler.SensorProtoHandler;
import app.model.sensor.event.impl.TemperatureSensorEvent;
import java.time.Instant;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto.PayloadCase;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;

@Component
public class TemperatureSensorProtoHandler implements SensorProtoHandler<TemperatureSensorEvent> {

    @Override
    public PayloadCase getMessageType() {
        return PayloadCase.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public TemperatureSensorEvent handle(SensorEventProto sensorEventProto) {
        TemperatureSensorEvent event = new TemperatureSensorEvent();
        TemperatureSensorProto eventProto = sensorEventProto.getTemperatureSensorEvent();

        event.setId(sensorEventProto.getId());
        event.setHubId(sensorEventProto.getHubId());
        event.setTimestamp(Instant.ofEpochSecond(sensorEventProto.getTimestamp().getSeconds()));
        event.setTemperatureC(eventProto.getTemperatureC());
        event.setTemperatureF(eventProto.getTemperatureF());

        return event;
    }
}
