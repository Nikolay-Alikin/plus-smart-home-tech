package app.processor.impl;

import app.config.KafkaProperties;
import app.processor.Processor;
import app.service.HubService;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventProcessor implements Processor {

    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(2000);

    @Qualifier("eventService")
    private final HubService<HubEventAvro> hubService;
    private final KafkaProperties kafkaProperties;

    @Value("${kafka.topic.hub.name}")
    private String topic;

    @Override
    public void run() {
        try (KafkaConsumer<Void, HubEventAvro> consumer = new KafkaConsumer<>(
                kafkaProperties.getHubEventProperties())) {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

            consumer.subscribe(List.of(topic));

            while (true) {
                ConsumerRecords<Void, HubEventAvro> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);
                for (ConsumerRecord<Void, HubEventAvro> record : records) {
                    hubService.process(record.value());
                }
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий хаба", e);
        }
    }
}
