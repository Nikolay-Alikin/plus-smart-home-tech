package app.serialization.impl;

import app.serialization.GeneralAvroDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public class SensorEventDeserializer extends GeneralAvroDeserializer<SensorEventAvro> {

    public SensorEventDeserializer() {
        super(SensorEventAvro.class);
    }

    public SensorEventDeserializer(Class<SensorEventAvro> targetType) {
        super(targetType);
    }
}
