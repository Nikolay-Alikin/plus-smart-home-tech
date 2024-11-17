package ru.yandex.practicum.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.entity.OrderDeliveryEntity;

@Repository
public interface OrderDeliveryRepository extends JpaRepository<OrderDeliveryEntity, UUID> {

}
