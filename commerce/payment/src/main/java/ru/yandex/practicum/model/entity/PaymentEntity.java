package ru.yandex.practicum.model.entity;

import jakarta.persistence.Column;
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
import ru.yandex.practicum.model.enumerated.PaymentStatus;

@Entity
@Getter
@Setter
@Table(name = "payments")
public class PaymentEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID paymentId;
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus status;
    @Column
    private UUID orderId;
    @Column
    private BigDecimal totalPayment;
    @Column
    private BigDecimal deliveryTotal;
    @Column
    private BigDecimal feeTotal;
}
