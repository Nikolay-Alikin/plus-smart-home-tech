package ru.yandex.practicum.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.model.enumerated.State;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class OrderEntity {

    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "username", nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    @Column
    private UUID shoppingCartId;

    @OneToMany(mappedBy = "orderEntity", cascade = {jakarta.persistence.CascadeType.PERSIST,
            jakarta.persistence.CascadeType.MERGE, jakarta.persistence.CascadeType.REFRESH,
            jakarta.persistence.CascadeType.DETACH})
    private List<AddressEntity> addresses;

    @OneToMany(mappedBy = "orderEntity", cascade = {jakarta.persistence.CascadeType.PERSIST,
            jakarta.persistence.CascadeType.MERGE, jakarta.persistence.CascadeType.REFRESH,
            jakarta.persistence.CascadeType.DETACH})
    private List<ProductEntity> products;

    @Column
    private UUID paymentId;

    @Column
    private UUID deliveryId;

    @Column
    private double deliveryWeight;

    @Column
    private double deliveryVolume;

    @Column
    private boolean fragile;

    @Column
    private BigDecimal totalPrice;

    @Column
    private BigDecimal deliveryPrice;

    @Column
    private BigDecimal productPrice;
}
