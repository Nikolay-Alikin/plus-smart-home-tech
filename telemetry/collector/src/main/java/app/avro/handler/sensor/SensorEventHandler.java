package app.avro.handler.sensor;

import app.model.sensor.event.SensorEvent;
import org.apache.avro.specific.SpecificRecordBase;

public interface SensorEventHandler<T extends SensorEvent, R extends SpecificRecordBase> {

    R handleEvent(T event);
}
