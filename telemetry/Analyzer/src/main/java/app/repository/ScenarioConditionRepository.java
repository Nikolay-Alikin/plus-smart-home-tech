package app.repository;

import app.model.entity.ScenarioCondition;
import app.model.entity.Sensor;
import app.model.entity.id.ScenarioConditionId;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenarioConditionRepository extends JpaRepository<ScenarioCondition, ScenarioConditionId> {


    List<ScenarioCondition> getScenarioConditionsBySensorIn(Collection<Sensor> sensor);

    List<ScenarioCondition> getScenarioConditionsBySensor_Id(String id);
}
