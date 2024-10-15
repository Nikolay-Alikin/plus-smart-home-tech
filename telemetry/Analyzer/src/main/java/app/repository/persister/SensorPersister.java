package app.repository.persister;

import app.model.entity.Sensor;
import app.repository.SensorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class SensorPersister {

    private final SensorRepository repository;

    public Sensor save(Sensor sensor) {
        return repository.save(sensor);
    }

    public void removeById(String id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Sensor> getSensorsByIds(List<String> ids) {
        return repository.findAllById(ids);
    }

    @Transactional(readOnly = true)
    public List<Sensor> getSensorsByHubId(String id) {
        return repository.findAllByHubId(id).getContent();
    }
}
