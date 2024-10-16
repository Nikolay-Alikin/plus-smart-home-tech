package app.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

@Service
public class SnapShotService {

    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>(500);


    public Optional<SensorsSnapshotAvro> agregate(SensorEventAvro event) {
        SensorsSnapshotAvro snapshot = snapshots.getOrDefault(event.getHubid(), new SensorsSnapshotAvro());
        if (snapshot.getSensorsState().containsKey(event.getId())) {
            SensorStateAvro oldState = snapshot.getSensorsState().get(event.getId());
            if (oldState.getTimestamp().isAfter(event.getTimestamp())) {
                return Optional.empty();
            }
        }
        SensorStateAvro newState = new SensorStateAvro();

        newState.setTimestamp(event.getTimestamp());
        newState.setData(event.getPayload());
        snapshot.getSensorsState().put(event.getId(), newState);
        snapshots.put(event.getHubid(), snapshot);

        return Optional.of(snapshot);
    }
}
