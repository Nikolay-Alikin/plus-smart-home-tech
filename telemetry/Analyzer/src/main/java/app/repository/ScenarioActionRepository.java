package app.repository;

import app.model.entity.ScenarioAction;
import app.model.entity.Sensor;
import app.model.entity.id.ScenarioActionId;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenarioActionRepository extends JpaRepository<ScenarioAction, ScenarioActionId> {

    List<ScenarioAction> getScenarioActionsBySensorIn(Collection<Sensor> sensors);

    List<ScenarioAction> getScenarioActionsBySensor_Id(String id);
}
