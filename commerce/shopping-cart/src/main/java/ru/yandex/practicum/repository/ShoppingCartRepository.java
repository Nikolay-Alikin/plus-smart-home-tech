package ru.yandex.practicum.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.entity.ShoppingCartEntity;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCartEntity, UUID> {

    void deleteByUsername(String username);

    ShoppingCartEntity findByUsername(String username);
}
