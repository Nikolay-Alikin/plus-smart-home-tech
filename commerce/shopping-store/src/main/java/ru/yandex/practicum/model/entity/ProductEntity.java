package ru.yandex.practicum.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String imageSrc;

    @Column(name = "quantity_state", nullable = false)
    private String quantityState;

    @Column(name = "product_state", nullable = false)
    private String productState;

    @Column(name = "rating", nullable = false)
    private double rating;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "price", nullable = false)
    private BigDecimal price;
}

