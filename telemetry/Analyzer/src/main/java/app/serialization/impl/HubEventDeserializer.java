package app.serialization.impl;

import app.serialization.GeneralAvroDeserializer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
public class HubEventDeserializer extends GeneralAvroDeserializer<HubEventAvro> {

    public HubEventDeserializer() {
        super(HubEventAvro.class);
    }
}
