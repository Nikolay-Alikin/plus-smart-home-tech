package app.avro.handler.sensor.impl;

import app.avro.handler.sensor.SensorEventHandler;
import app.model.sensor.event.impl.MotionSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;


public class MotionSensorHandlerSensor implements SensorEventHandler<MotionSensorEvent, MotionSensorAvro> {

    @Override
    public MotionSensorAvro handleEvent(MotionSensorEvent event) {
        MotionSensorAvro sensor = new MotionSensorAvro();
        sensor.setLinkQuality(event.getLinkQuality());
        sensor.setMotion(event.isMotion());
        sensor.setVoltage(event.getVoltage());

        return sensor;
    }
}
