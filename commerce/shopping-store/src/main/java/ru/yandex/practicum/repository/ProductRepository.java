package ru.yandex.practicum.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    List<ProductEntity> findAllByProductCategory(String category, Pageable pageable);
}
