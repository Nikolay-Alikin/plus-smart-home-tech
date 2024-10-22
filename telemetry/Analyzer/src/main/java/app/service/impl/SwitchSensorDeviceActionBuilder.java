package app.service.impl;

import static ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro.SWITCH;

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
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Service
public class SwitchSensorDeviceActionBuilder implements DeviceActionBuilder<SwitchSensorAvro> {

    private final Predicate<ScenarioActionConditionDto> filterByType = scenarioCondition -> {
        String type = scenarioCondition.getCondition().getType();
        return type.equals(SWITCH.name());
    };

    @Override
    public List<DeviceActionRequest> build(SwitchSensorAvro data, List<ScenarioActionConditionDto> dtos) {
        List<DeviceActionRequest> requests = new ArrayList<>();
        dtos.stream()
                .filter(filterByType)
                .forEach(scenarioActionConditionDto -> {
                    Sensor sensor = scenarioActionConditionDto.getSensor();
                    Condition condition = scenarioActionConditionDto.getCondition();
                    Action action = scenarioActionConditionDto.getAction();
                    int switchValue = data.getState() ? 1 : 0;
                    switch (condition.getOperation()) {
                        case "БОЛЬШЕ" -> {
                            DeviceActionRequest request = buildRequestDto(sensor.getId(), action.getType(),
                                    action.getValue());
                            if (switchValue > condition.getValue()) {
                                requests.add(request);
                            }
                        }
                        case "МЕНЬШЕ" -> {
                            DeviceActionRequest request = buildRequestDto(sensor.getId(), action.getType(),
                                    action.getValue());
                            if (switchValue < condition.getValue()) {
                                requests.add(request);
                            }
                        }
                        case "РАВНО" -> {
                            DeviceActionRequest request = buildRequestDto(sensor.getId(), action.getType(),
                                    action.getValue());
                            if (switchValue == condition.getValue()) {
                                requests.add(request);
                            }
                        }
                    }
                });
        return requests;
    }
}
