package app.avro.handler.hub.impl;

import app.avro.handler.hub.HubEventHandler;
import app.model.hub.event.impl.ScenarioRemovedEvent;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

public class ScenarioRemovedEventHandler implements HubEventHandler<ScenarioRemovedEvent, ScenarioRemovedEventAvro> {

    @Override
    public ScenarioRemovedEventAvro handleEvent(ScenarioRemovedEvent event) {
        ScenarioRemovedEventAvro scenarioRemovedEventAvro = new ScenarioRemovedEventAvro();
        scenarioRemovedEventAvro.setName(event.getName());
        return scenarioRemovedEventAvro;
    }
}
