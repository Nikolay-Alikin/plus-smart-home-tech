package ru.yandex.practicum.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;


@Entity
@Getter
@Setter
@Table(name = "products")
public class ProductEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;
    @Column(name = "count")
    private long count;
    @JoinColumn(name = "shopping_cart_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ShoppingCartEntity shoppingCartEntity;

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Class<?> oEffectiveClass =
                o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer()
                        .getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass =
                this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer()
                        .getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) {
            return false;
        }
        ProductEntity productEntity = (ProductEntity) o;
        return getId() != null && Objects.equals(getId(), productEntity.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass()
                .hashCode()
                : getClass().hashCode();
    }
}
