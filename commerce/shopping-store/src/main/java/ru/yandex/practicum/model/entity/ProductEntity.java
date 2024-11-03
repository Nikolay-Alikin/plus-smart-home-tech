package ru.yandex.practicum.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.yandex.practicum.generated.model.dto.ProductCategory;
import ru.yandex.practicum.generated.model.dto.ProductState;
import ru.yandex.practicum.generated.model.dto.QuantityState;
import ru.yandex.practicum.generated.model.dto.SetProductQuantityStateRequest.QuantityStateEnum;

@Entity
@Getter
@Setter
@Table(name = "products")
@Accessors(fluent = true)
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id", nullable = false)
    private UUID id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image_src")
    private String imageSrc;

    @Column(name = "quantity_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private QuantityStateEnum quantityState;

    @Column(name = "product_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductState productState;

    @Column(name = "rating", nullable = false)
    private double rating;

    @Column(name = "product_category")
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @Column(name = "price", nullable = false)
    private BigDecimal price;
}

