package app.model.sensor.event.impl;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import app.model.sensor.enumerated.SensorEventType;
import app.model.sensor.event.SensorEvent;


@Getter
@Setter
@ToString(callSuper = true)
public class MotionSensorEvent extends SensorEvent {

    @NotNull
    private int linkQuality;
    @NotNull
    private boolean motion;
    @NotNull
    private int voltage;


    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
