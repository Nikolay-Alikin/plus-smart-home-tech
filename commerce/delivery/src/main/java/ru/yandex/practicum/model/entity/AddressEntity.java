package ru.yandex.practicum.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@Entity
@Table(name = "addresses")
public class AddressEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String country;
    private String city;
    private String street;
    private String house;
    private String flat;
    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private DeliveryEntity deliveryEntity;
    private boolean isFrom;
}
