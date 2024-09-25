package app.avro;

import app.avro.handler.sensor.SensorEventHandler;
import app.avro.handler.sensor.SensorHandlerFactory;
import app.avro.handler.sensor.impl.ClimateSensorHandlerSensor;
import app.avro.handler.sensor.impl.LightSensorHandlerSensor;
import app.avro.handler.sensor.impl.MotionSensorHandlerSensor;
import app.avro.handler.sensor.impl.SwitchSensorHandlerSensor;
import app.avro.handler.sensor.impl.TemperatureSensorHandlerSensor;
import app.model.sensor.event.SensorEvent;
import app.model.sensor.event.impl.ClimateSensorEvent;
import app.model.sensor.event.impl.LightSensorEvent;
import app.model.sensor.event.impl.MotionSensorEvent;
import app.model.sensor.event.impl.SwitchSensorEvent;
import app.model.sensor.event.impl.TemperatureSensorEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorRecordBuilder implements RecordBuilder<SensorEvent> {

    @Override
    public SpecificRecordBase buildSpecificRecordBase(SensorEvent sensorEvent) {
        SensorEventAvro sensorEventAvro = new SensorEventAvro();
        sensorEventAvro.setId(sensorEvent.getId());
        sensorEventAvro.setHubid(sensorEvent.getHubId());
        sensorEventAvro.setTimestamp(sensorEvent.getTimestamp());

        Object payload = switch (sensorEvent.getType()) {
            case LIGHT_SENSOR_EVENT -> toLightSensorRecord(sensorEvent);
            case CLIMATE_SENSOR_EVENT -> toClimateSensorRecord(sensorEvent);
            case MOTION_SENSOR_EVENT -> toMotionSensorRecord(sensorEvent);
            case SWITCH_SENSOR_EVENT -> toSwitchSensorRecord(sensorEvent);
            case TEMPERATURE_SENSOR_EVENT -> toTemperatureSensorRecord(sensorEvent);
        };
        sensorEventAvro.setPayload(payload);
        return sensorEventAvro;
    }


    private LightSensorAvro toLightSensorRecord(SensorEvent sensorEvent) {
        LightSensorHandlerSensor handler = (LightSensorHandlerSensor) getHandler(sensorEvent);
        return handler.handleEvent((LightSensorEvent) sensorEvent);
    }

    private ClimateSensorAvro toClimateSensorRecord(SensorEvent sensorEvent) {
        ClimateSensorHandlerSensor handler = (ClimateSensorHandlerSensor) getHandler(sensorEvent);
        return handler.handleEvent((ClimateSensorEvent) sensorEvent);
    }

    private MotionSensorAvro toMotionSensorRecord(SensorEvent sensorEvent) {
        MotionSensorHandlerSensor handler = (MotionSensorHandlerSensor) getHandler(sensorEvent);
        return handler.handleEvent((MotionSensorEvent) sensorEvent);
    }

    private SwitchSensorAvro toSwitchSensorRecord(SensorEvent sensorEvent) {
        SwitchSensorHandlerSensor handler = (SwitchSensorHandlerSensor) getHandler(sensorEvent);
        return handler.handleEvent((SwitchSensorEvent) sensorEvent);
    }

    private TemperatureSensorAvro toTemperatureSensorRecord(SensorEvent sensorEvent) {
        TemperatureSensorHandlerSensor handler = (TemperatureSensorHandlerSensor) getHandler(sensorEvent);
        return handler.handleEvent((TemperatureSensorEvent) sensorEvent);
    }

    @SuppressWarnings("rawtypes")
    private SensorEventHandler getHandler(SensorEvent sensorEvent) {
        SensorEventHandler handler = SensorHandlerFactory.getHandler(sensorEvent.getClass());
        if (handler == null) {
            log.error("Неизвестный тип события={}", sensorEvent);
            throw new IllegalArgumentException("Неизвестный тип события");
        }
        return handler;
    }
}
