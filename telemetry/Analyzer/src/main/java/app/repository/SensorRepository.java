package app.repository;

import app.model.entity.Sensor;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, String> {

    Slice<Sensor> findAllByHubId(String hubId);
}
