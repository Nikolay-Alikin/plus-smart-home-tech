package app.repository;

import app.model.entity.Scenario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

    Optional<Scenario> findByHubIdAndName(String hubId, String name);
}
