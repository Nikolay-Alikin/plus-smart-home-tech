package ru.yandex.practicum.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "address")
public class AddressProperties {

    private String country;
    private String city;
    private String street;
    private String house;
    private String flat;
}
