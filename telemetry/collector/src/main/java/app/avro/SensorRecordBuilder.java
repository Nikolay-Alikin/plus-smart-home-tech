package app.avro;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import app.model.sensor.event.SensorEvent;
import app.model.sensor.event.impl.ClimateSensorEvent;
import app.model.sensor.event.impl.LightSensorEvent;
import app.model.sensor.event.impl.MotionSensorEvent;
import app.model.sensor.event.impl.SwitchSensorEvent;
import app.model.sensor.event.impl.TemperatureSensorEvent;
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
        LightSensorEvent lightSensorEvent;
        if (sensorEvent instanceof LightSensorEvent) {
            lightSensorEvent = (LightSensorEvent) sensorEvent;
        } else {
            log.error("Невозможно привести событие={} к LightSensorEvent", sensorEvent);
            throw new ClassCastException("Невозможно привести событие к LightSensorEvent");
        }
        LightSensorAvro sensor = new LightSensorAvro();
        sensor.setLinkQuality(lightSensorEvent.getLinkQuality());
        sensor.setLuminosity(lightSensorEvent.getLuminosity());

        return sensor;
    }

    private ClimateSensorAvro toClimateSensorRecord(SensorEvent sensorEvent) {
        ClimateSensorEvent climateSensorEvent;
        if (sensorEvent instanceof ClimateSensorEvent) {
            climateSensorEvent = (ClimateSensorEvent) sensorEvent;
        } else {
            log.error("Невозможно привести событие={} к ClimateSensorEvent", sensorEvent);
            throw new ClassCastException("Невозможно привести событие к ClimateSensorEvent");
        }
        ClimateSensorAvro sensor = new ClimateSensorAvro();
        sensor.setTemperatureC(climateSensorEvent.getTemperatureC());
        sensor.setHumidity(climateSensorEvent.getHumidity());
        sensor.setCo2Level(climateSensorEvent.getCo2Level());

        return sensor;
    }

    private MotionSensorAvro toMotionSensorRecord(SensorEvent sensorEvent) {
        MotionSensorEvent motionSensorEvent;
        if (sensorEvent instanceof MotionSensorEvent) {
            motionSensorEvent = (MotionSensorEvent) sensorEvent;
        } else {
            log.error("Невозможно привести событие={} к MotionSensorEvent", sensorEvent);
            throw new ClassCastException("Невозможно привести событие к MotionSensorEvent");
        }
        MotionSensorAvro sensor = new MotionSensorAvro();
        sensor.setLinkQuality(motionSensorEvent.getLinkQuality());
        sensor.setMotion(motionSensorEvent.isMotion());
        sensor.setVoltage(motionSensorEvent.getVoltage());

        return sensor;
    }

    private SwitchSensorAvro toSwitchSensorRecord(SensorEvent sensorEvent) {
        SwitchSensorEvent switchSensorEvent;
        if (sensorEvent instanceof SwitchSensorEvent) {
            switchSensorEvent = (SwitchSensorEvent) sensorEvent;
        } else {
            log.error("Невозможно привести событие={} к SwitchSensorEvent", sensorEvent);
            throw new ClassCastException("Невозможно привести событие к SwitchSensorEvent");
        }

        SwitchSensorAvro sensor = new SwitchSensorAvro();
        sensor.setState(switchSensorEvent.isState());

        return sensor;
    }

    private TemperatureSensorAvro toTemperatureSensorRecord(SensorEvent sensorEvent) {
        TemperatureSensorEvent temperatureSensorEvent;
        if (sensorEvent instanceof TemperatureSensorEvent) {
            temperatureSensorEvent = (TemperatureSensorEvent) sensorEvent;
        } else {
            log.error("Невозможно привести событие={} к TemperatureSensorEvent", sensorEvent);
            throw new ClassCastException("Невозможно привести событие к TemperatureSensorEvent");
        }
        TemperatureSensorAvro sensor = new TemperatureSensorAvro();
        sensor.setId(sensorEvent.getId());
        sensor.setHubId(sensorEvent.getHubId());
        sensor.setTimestamp(sensorEvent.getTimestamp());
        sensor.setTemperatureC(temperatureSensorEvent.getTemperatureC());
        sensor.setTemperatureF(temperatureSensorEvent.getTemperatureF());
        return sensor;
    }
}
