package app.controller;

import app.model.hub.event.HubEvent;
import app.model.sensor.event.SensorEvent;
import app.service.TransmitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    @Qualifier("sensorService")
    private final TransmitService<SensorEvent> sensorService;
    @Qualifier("hubService")
    private final TransmitService<HubEvent> hubService;

    @PostMapping("/sensors")
    @ResponseStatus(HttpStatus.OK)
    public void transmitSensorEvent(@Valid @RequestBody SensorEvent sensorEvent) {
        log.info("Поступил запрос на отправку события сенсора={}", sensorEvent);
        sensorService.transmit(sensorEvent);
    }

    @PostMapping("/hubs")
    @ResponseStatus(HttpStatus.OK)
    public void transmitHubEvent(@Valid @RequestBody HubEvent hubEvent) {
        log.info("Поступил запрос на отправку события хаба={}", hubEvent);
        hubService.transmit(hubEvent);
    }
}
