package app.config;

import app.serialization.GeneralAvroSerializer;
import app.serialization.impl.SensorEventDeserializer;
import jakarta.annotation.PostConstruct;
import java.util.Properties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Setter
@Component
@RequiredArgsConstructor
public class KafkaProperties {

    @Getter
    private Properties consumerConfig;
    @Getter
    private Properties producerConfig;

    @Value("${kafka.bootstrapServers}")
    private String bootstrapServers;


    @PostConstruct
    public void init() {
        //Consumer
        consumerConfig = new Properties();
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class);
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorEventDeserializer.class);
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "aggregators");

        //Producer
        producerConfig = new Properties();
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class);
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);
    }
}
