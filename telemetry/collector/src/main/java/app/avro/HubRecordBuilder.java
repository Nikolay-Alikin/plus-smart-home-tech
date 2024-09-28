package app.avro;

import app.avro.handler.hub.HubEventHandler;
import app.avro.handler.hub.HubHandlerFactory;
import app.avro.handler.hub.impl.DeviceAddedEventHandler;
import app.avro.handler.hub.impl.DeviceRemovedEventHandler;
import app.avro.handler.hub.impl.ScenarioAddedEventHandler;
import app.avro.handler.hub.impl.ScenarioRemovedEventHandler;
import app.model.hub.event.HubEvent;
import app.model.hub.event.impl.DeviceAddedEvent;
import app.model.hub.event.impl.DeviceRemovedEvent;
import app.model.hub.event.impl.ScenarioAddedEvent;
import app.model.hub.event.impl.ScenarioRemovedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubRecordBuilder implements RecordBuilder<HubEvent> {

    @Override
    public SpecificRecordBase buildSpecificRecordBase(HubEvent event) {
        HubEventAvro hubEventAvro = new HubEventAvro();
        hubEventAvro.setHubId(event.getHubId());
        hubEventAvro.setTimestamp(event.getTimestamp());

        Object payload = switch (event.getType()) {
            case DEVICE_ADDED -> toDeviceAddedEvent(event);
            case DEVICE_REMOVED -> toDeviceRemovedEvent(event);
            case SCENARIO_ADDED -> toScenarioAddedEvent(event);
            case SCENARIO_REMOVED -> toScenarioRemovedEvent(event);
        };
        hubEventAvro.setPayload(payload);

        return hubEventAvro;
    }

    private DeviceAddedEventAvro toDeviceAddedEvent(HubEvent event) {
        DeviceAddedEventHandler handler = (DeviceAddedEventHandler) getHandler(event);
        return handler.handleEvent((DeviceAddedEvent) event);
    }

    private DeviceRemovedEventAvro toDeviceRemovedEvent(HubEvent event) {
        DeviceRemovedEventHandler handler = (DeviceRemovedEventHandler) getHandler(event);
        return handler.handleEvent((DeviceRemovedEvent) event);
    }

    private ScenarioAddedEventAvro toScenarioAddedEvent(HubEvent event) {
        ScenarioAddedEventHandler handler = (ScenarioAddedEventHandler) getHandler(event);
        return handler.handleEvent((ScenarioAddedEvent) event);
    }

    private ScenarioRemovedEventAvro toScenarioRemovedEvent(HubEvent event) {
        ScenarioRemovedEventHandler handler = (ScenarioRemovedEventHandler) getHandler(event);
        return handler.handleEvent((ScenarioRemovedEvent) event);
    }

    @SuppressWarnings("rawtypes")
    private HubEventHandler getHandler(HubEvent hubEvent) {
        HubEventHandler handler = HubHandlerFactory.getHandler(hubEvent.getClass());
        if (handler == null) {
            log.error("Неизвестный тип события={}", hubEvent);
            throw new IllegalArgumentException("Неизвестный тип события");
        }
        return handler;
    }
}
