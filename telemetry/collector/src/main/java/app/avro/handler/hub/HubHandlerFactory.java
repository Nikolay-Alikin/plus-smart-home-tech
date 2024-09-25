package app.avro.handler.hub;

import app.avro.handler.hub.impl.DeviceAddedEventHandler;
import app.avro.handler.hub.impl.DeviceRemovedEventHandler;
import app.avro.handler.hub.impl.ScenarioAddedEventHandler;
import app.avro.handler.hub.impl.ScenarioRemovedEventHandler;
import app.model.hub.event.HubEvent;
import app.model.hub.event.impl.DeviceAddedEvent;
import app.model.hub.event.impl.DeviceRemovedEvent;
import app.model.hub.event.impl.ScenarioAddedEvent;
import app.model.hub.event.impl.ScenarioRemovedEvent;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class HubHandlerFactory {

    private static final Map<Class<? extends HubEvent>, HubEventHandler<? extends HubEvent, ?>> handlers = new HashMap<>();

    static {
        handlers.put(DeviceAddedEvent.class, new DeviceAddedEventHandler());
        handlers.put(DeviceRemovedEvent.class, new DeviceRemovedEventHandler());
        handlers.put(ScenarioAddedEvent.class, new ScenarioAddedEventHandler());
        handlers.put(ScenarioRemovedEvent.class, new ScenarioRemovedEventHandler());
    }

    public static <T extends HubEvent> HubEventHandler<? extends HubEvent, ?> getHandler(Class<T> eventClass) {
        return handlers.get(eventClass);
    }

}
