package app.service.impl;

import app.avro.RecordBuilder;
import app.model.hub.event.HubEvent;
import app.service.KafkaSender;
import app.service.TransmitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubService implements TransmitService<HubEvent> {

    private final KafkaSender sender;
    @Qualifier("hubRecordBuilder")
    private final RecordBuilder<HubEvent> recordBuilder;

    @Value("${kafka.topic.hubs.name}")
    private String topic;


    @Override
    public void transmit(HubEvent event) {
        SpecificRecordBase recordBase = recordBuilder.buildSpecificRecordBase(event);
        sender.send(recordBase, topic);
    }
}
