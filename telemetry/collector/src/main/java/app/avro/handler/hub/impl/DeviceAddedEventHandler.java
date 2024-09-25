package app.avro.handler.hub.impl;

import app.avro.handler.hub.HubEventHandler;
import app.model.hub.event.impl.DeviceAddedEvent;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

public class DeviceAddedEventHandler implements HubEventHandler<DeviceAddedEvent, DeviceAddedEventAvro> {

    @Override
    public DeviceAddedEventAvro handleEvent(DeviceAddedEvent event) {
        DeviceAddedEventAvro deviceAddedEventAvro = new DeviceAddedEventAvro();
        deviceAddedEventAvro.setId(event.getId());
        deviceAddedEventAvro.setType(DeviceTypeAvro.valueOf(event.getDeviceType()));

        return deviceAddedEventAvro;
    }
}
