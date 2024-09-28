package app.avro.handler.sensor.impl;

import app.avro.handler.sensor.SensorEventHandler;
import app.model.sensor.event.impl.SwitchSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

public class SwitchSensorHandlerSensor implements SensorEventHandler<SwitchSensorEvent, SwitchSensorAvro> {

    @Override
    public SwitchSensorAvro handleEvent(SwitchSensorEvent event) {
        SwitchSensorAvro sensor = new SwitchSensorAvro();
        sensor.setState(event.isState());

        return sensor;
    }
}
