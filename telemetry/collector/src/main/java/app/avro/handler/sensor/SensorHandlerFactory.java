package app.avro.handler.sensor;

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
import java.util.HashMap;
import java.util.Map;

public class SensorHandlerFactory {

    private static final Map<Class<? extends SensorEvent>, SensorEventHandler<? extends SensorEvent, ?>> handlers = new HashMap<>();

    static {
        handlers.put(LightSensorEvent.class, new LightSensorHandlerSensor());
        handlers.put(ClimateSensorEvent.class, new ClimateSensorHandlerSensor());
        handlers.put(MotionSensorEvent.class, new MotionSensorHandlerSensor());
        handlers.put(SwitchSensorEvent.class, new SwitchSensorHandlerSensor());
        handlers.put(TemperatureSensorEvent.class, new TemperatureSensorHandlerSensor());
    }

    public static <T extends SensorEvent> SensorEventHandler<? extends SensorEvent, ?> getHandler(Class<T> eventClass) {
        return handlers.get(eventClass);
    }
}
