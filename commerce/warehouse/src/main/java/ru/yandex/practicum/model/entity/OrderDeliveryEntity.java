package ru.yandex.practicum.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Setter
@Getter
@Table(name = "orders_deliveries")
public class OrderDeliveryEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    private UUID orderId;
    private UUID deliveryId;

}
