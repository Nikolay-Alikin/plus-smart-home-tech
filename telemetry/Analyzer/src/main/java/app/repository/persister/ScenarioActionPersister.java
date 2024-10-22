package app.repository.persister;

import app.model.entity.ScenarioAction;
import app.repository.ScenarioActionRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class ScenarioActionPersister {

    private final ScenarioActionRepository scenarioActionRepository;

    public List<ScenarioAction> getScenarioActionsBySensorId(String sensorId) {
        return scenarioActionRepository.getScenarioActionsBySensor_Id(sensorId);
    }
}
