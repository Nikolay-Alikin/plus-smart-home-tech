package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.yandex.practicum.config.AddressProperties;

@SpringBootApplication
@EnableConfigurationProperties(AddressProperties.class)
public class Warehouse {

    public static void main(String[] args) {
        SpringApplication.run(Warehouse.class, args);
    }
}
