package app.service.impl;

import static ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro.LUMINOSITY;

import app.model.dto.ScenarioActionConditionDto;
import app.model.entity.Action;
import app.model.entity.Condition;
import app.model.entity.Sensor;
import app.service.DeviceActionBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Service
public class LightSensorDeviceActionBuilder implements DeviceActionBuilder<LightSensorAvro> {

    private final Predicate<ScenarioActionConditionDto> filterByType = scenarioCondition -> {
        String type = scenarioCondition.getCondition().getType();
        return type.equals(LUMINOSITY.name());
    };

    @Override
    public List<DeviceActionRequest> build(LightSensorAvro data, List<ScenarioActionConditionDto> dtos) {
        List<DeviceActionRequest> requests = new ArrayList<>();
        dtos.stream()
                .filter(filterByType)
                .forEach(scenarioActionConditionDto -> {
                    Sensor sensor = scenarioActionConditionDto.getSensor();
                    Condition condition = scenarioActionConditionDto.getCondition();
                    Action action = scenarioActionConditionDto.getAction();

                    switch (condition.getOperation()) {
                        case "БОЛЬШЕ" -> {
                            DeviceActionRequest request = buildRequestDto(sensor.getId(), action.getType(),
                                    action.getValue());
                            if (data.getLuminosity() > condition.getValue()) {
                                requests.add(request);
                            }
                        }
                        case "МЕНЬШЕ" -> {
                            DeviceActionRequest request = buildRequestDto(sensor.getId(), action.getType(),
                                    action.getValue());
                            if (data.getLuminosity() < condition.getValue()) {
                                requests.add(request);
                            }
                        }
                        case "РАВНО" -> {
                            DeviceActionRequest request = buildRequestDto(sensor.getId(), action.getType(),
                                    action.getValue());
                            if (data.getLuminosity() == condition.getValue()) {
                                requests.add(request);
                            }
                        }
                    }
                });
        return requests;
    }
}
