package ru.yandex.practicum.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    Optional<List<OrderEntity>> findByUsername(String username);
}
