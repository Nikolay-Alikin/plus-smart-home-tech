package ru.yandex.practicum.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.entity.AddressEntity;
import ru.yandex.practicum.model.entity.OrderEntity;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, UUID> {

    AddressEntity findByOrderEntity(OrderEntity orderEntity);
}
