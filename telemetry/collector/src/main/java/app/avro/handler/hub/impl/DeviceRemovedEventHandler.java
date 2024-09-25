package app.avro.handler.hub.impl;

import app.avro.handler.hub.HubEventHandler;
import app.model.hub.event.impl.DeviceRemovedEvent;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

public class DeviceRemovedEventHandler implements HubEventHandler<DeviceRemovedEvent, DeviceRemovedEventAvro> {


    @Override
    public DeviceRemovedEventAvro handleEvent(DeviceRemovedEvent event) {
        DeviceRemovedEventAvro deviceRemovedEventAvro = new DeviceRemovedEventAvro();
        deviceRemovedEventAvro.setId(event.getId());
        return deviceRemovedEventAvro;
    }
}
