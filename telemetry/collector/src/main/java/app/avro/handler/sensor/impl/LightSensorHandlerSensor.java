package app.avro.handler.sensor.impl;

import app.avro.handler.sensor.SensorEventHandler;
import app.model.sensor.event.impl.LightSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;


public class LightSensorHandlerSensor implements SensorEventHandler<LightSensorEvent, LightSensorAvro> {

    @Override
    public LightSensorAvro handleEvent(LightSensorEvent event) {
        LightSensorAvro sensor = new LightSensorAvro();
        sensor.setLinkQuality(event.getLinkQuality());
        sensor.setLuminosity(event.getLuminosity());
        return sensor;
    }
}
