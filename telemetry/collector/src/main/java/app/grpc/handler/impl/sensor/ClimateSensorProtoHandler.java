package app.grpc.handler.impl.sensor;

import app.grpc.handler.SensorProtoHandler;
import app.model.sensor.event.impl.ClimateSensorEvent;
import java.time.Instant;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto.PayloadCase;

@Component
public class ClimateSensorProtoHandler implements SensorProtoHandler<ClimateSensorEvent> {

    @Override
    public PayloadCase getMessageType() {
        return PayloadCase.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public ClimateSensorEvent handle(SensorEventProto sensorEventProto) {
        ClimateSensorEvent event = new ClimateSensorEvent();
        ClimateSensorProto eventProto = sensorEventProto.getClimateSensorEvent();

        event.setId(sensorEventProto.getId());
        event.setHubId(sensorEventProto.getHubId());
        event.setTimestamp(Instant.ofEpochSecond(sensorEventProto.getTimestamp().getSeconds()));
        event.setTemperatureC(eventProto.getTemperatureC());
        event.setHumidity(eventProto.getHumidity());
        event.setCo2Level(eventProto.getCo2Level());

        return event;
    }
}
