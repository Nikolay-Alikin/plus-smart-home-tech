package app.service.impl;

import app.avro.RecordBuilder;
import app.model.sensor.event.SensorEvent;
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
public class SensorService implements TransmitService<SensorEvent> {

    private final KafkaSender kafkaSender;
    @Qualifier("sensorRecordBuilder")
    private final RecordBuilder<SensorEvent> recordBuilder;

    @Value("${topic.sensors.name}")
    private String topic;

    @Override
    public void transmit(SensorEvent sensorEvent) {
        SpecificRecordBase record = recordBuilder.buildSpecificRecordBase(sensorEvent);
        kafkaSender.send(record,topic);
    }
}
