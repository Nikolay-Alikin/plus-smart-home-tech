package app.config;

import app.serialization.impl.SensorsSnapshotDeserializer;
import app.serialization.impl.SpecificRecordBaseDeserializer;
import jakarta.annotation.PostConstruct;
import java.util.Properties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Setter
@Component
@RequiredArgsConstructor
public class KafkaProperties {

    @Getter
    private Properties hubEventProperties;
    @Getter
    private Properties snapshotsProperties;

    @Value("${kafka.bootstrapServers}")
    private String bootstrapServers;
    @Value("${kafka.commit}")
    private String autoCommitIntervalMs;


    @PostConstruct
    public void init() {
        //hubEvent
        hubEventProperties = new Properties() {
            {
                put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
                put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
                put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitIntervalMs);
                put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class);
                put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SpecificRecordBaseDeserializer.class);
                put(ConsumerConfig.GROUP_ID_CONFIG, "hub");
            }
        };
        //hubEvent
        snapshotsProperties = new Properties() {
            {
                put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
                put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
                put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitIntervalMs);
                put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class);
                put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorsSnapshotDeserializer.class);
                put(ConsumerConfig.GROUP_ID_CONFIG, "snapshots");
            }
        };
    }
}
