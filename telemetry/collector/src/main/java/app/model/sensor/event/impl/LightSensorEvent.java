package app.model.sensor.event.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import app.model.sensor.enumerated.SensorEventType;
import app.model.sensor.event.SensorEvent;

@Getter
@Setter
@ToString(callSuper = true)
public class LightSensorEvent extends SensorEvent {

    private int linkQuality;
    private int luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
