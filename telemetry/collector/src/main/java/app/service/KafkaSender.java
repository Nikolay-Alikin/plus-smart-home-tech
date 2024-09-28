package app.service;

import org.apache.avro.specific.SpecificRecordBase;

public interface KafkaSender {

   void send(SpecificRecordBase record, String topic);
}
