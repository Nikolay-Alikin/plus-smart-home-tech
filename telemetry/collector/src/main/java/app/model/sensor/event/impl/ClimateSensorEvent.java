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
public class ClimateSensorEvent extends SensorEvent {

    @NotNull
    private int temperatureC;
    @NotNull
    private int humidity;
    @NotNull
    private int co2Level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
