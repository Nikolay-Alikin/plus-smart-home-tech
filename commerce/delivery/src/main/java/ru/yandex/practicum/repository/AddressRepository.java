package ru.yandex.practicum.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.entity.AddressEntity;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, UUID> {

    Optional<AddressEntity> findByDeliveryEntityDeliveryIdAndFromIsTrue(UUID deliveryEntityId);
    Optional<AddressEntity> findByDeliveryEntityDeliveryIdAndFromIsFalse(UUID deliveryEntityId);
}
