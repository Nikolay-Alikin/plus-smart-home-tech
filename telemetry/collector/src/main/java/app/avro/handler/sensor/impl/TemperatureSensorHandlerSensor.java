package app.avro.handler.sensor.impl;

import app.avro.handler.sensor.SensorEventHandler;
import app.model.sensor.event.impl.TemperatureSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

public class TemperatureSensorHandlerSensor implements
        SensorEventHandler<TemperatureSensorEvent, TemperatureSensorAvro> {

    @Override
    public TemperatureSensorAvro handleEvent(TemperatureSensorEvent event) {
        TemperatureSensorAvro sensor = new TemperatureSensorAvro();
        sensor.setId(event.getId());
        sensor.setHubId(event.getHubId());
        sensor.setTimestamp(event.getTimestamp());
        sensor.setTemperatureC(event.getTemperatureC());
        sensor.setTemperatureF(event.getTemperatureF());
        return sensor;
    }
}
