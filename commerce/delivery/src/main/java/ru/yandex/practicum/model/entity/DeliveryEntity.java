package ru.yandex.practicum.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.model.enumerated.DeliveryState;

@Setter
@Getter
@Entity
@Table(name = "delivery")
public class DeliveryEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID deliveryId;

    private UUID orderId;

    @Enumerated(value = EnumType.STRING)
    private DeliveryState deliveryState;

    private BigDecimal totalWeight;

    private boolean fragile;
}

