package ru.yandex.practicum.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.entity.BookingEntity;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    List<BookingEntity> findAllByShoppingCartId(UUID shoppingCartId);

    void deleteAllByShoppingCartId(UUID shoppingCartId);
}
