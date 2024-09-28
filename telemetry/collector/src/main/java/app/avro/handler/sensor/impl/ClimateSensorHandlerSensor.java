package app.avro.handler.sensor.impl;

import app.avro.handler.sensor.SensorEventHandler;
import app.model.sensor.event.impl.ClimateSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

public class ClimateSensorHandlerSensor implements SensorEventHandler<ClimateSensorEvent, ClimateSensorAvro> {

    @Override
    public ClimateSensorAvro handleEvent(ClimateSensorEvent event) {
        ClimateSensorAvro sensor = new ClimateSensorAvro();
        sensor.setTemperatureC(event.getTemperatureC());
        sensor.setHumidity(event.getHumidity());
        sensor.setCo2Level(event.getCo2Level());

        return sensor;
    }
}
