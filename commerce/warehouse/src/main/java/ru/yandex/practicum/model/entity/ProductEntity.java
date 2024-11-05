package ru.yandex.practicum.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.yandex.practicum.model.entity.jsonB.DimensionJson;

@Entity
@Getter
@Setter
@Table(name = "products")
@Accessors(chain = true)
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "fragile")
    private boolean fragile;

    @Column(name = "dimension")
    @JdbcTypeCode(SqlTypes.JSON)
    private DimensionJson dimension;

    @Column(name = "weight", nullable = false)
    private double weight;

    @Column(name = "quantity")
    private long quantity;
}
