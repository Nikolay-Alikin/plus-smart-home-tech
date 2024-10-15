package app.repository.persister;

import app.model.entity.ScenarioCondition;
import app.model.entity.Sensor;
import app.repository.ScenarioConditionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ScenarioConditionPersister {

    private final ScenarioConditionRepository scenarioConditionRepository;

    @Transactional(readOnly = true)
    public List<ScenarioCondition> getScenarioConditionsBySensorId(String sensorId) {
        return scenarioConditionRepository.getScenarioConditionsBySensor_Id(sensorId);
    }
}
