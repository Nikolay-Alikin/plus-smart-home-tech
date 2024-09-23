package app.service.impl;

import app.config.KafkaConfig;
import app.service.KafkaSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaSenderImpl implements KafkaSender {

    @Override
    public void send(SpecificRecordBase record, String topic) {

        ProducerRecord<String, SpecificRecordBase> producerRecord = new ProducerRecord<>(topic, record);

        try (KafkaProducer<String, SpecificRecordBase> producer = new KafkaProducer<>(
                KafkaConfig.getProducerConfig())) {
            producer.send(producerRecord);
            log.debug(" [{}] Событие {} отправлено ", topic, record);
        } catch (Exception e) {
            log.error(" [{}] Ошибка отправки события {} ", topic, record, e);
            throw new RuntimeException(e);
        }
    }
}
