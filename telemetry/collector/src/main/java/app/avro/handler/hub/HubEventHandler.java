package app.avro.handler.hub;

import app.model.hub.event.HubEvent;
import org.apache.avro.specific.SpecificRecordBase;

public interface HubEventHandler<T extends HubEvent, R extends SpecificRecordBase> {

    R handleEvent(T event);
}
