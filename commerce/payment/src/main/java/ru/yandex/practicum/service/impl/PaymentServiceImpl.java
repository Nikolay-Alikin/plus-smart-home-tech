package ru.yandex.practicum.service.impl;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.client.ShoppingStoreClient;
import ru.yandex.practicum.generated.model.dto.NoOrderFoundException;
import ru.yandex.practicum.generated.model.dto.NoOrderFoundException.HttpStatusEnum;
import ru.yandex.practicum.generated.model.dto.OrderDto;
import ru.yandex.practicum.generated.model.dto.PaymentDto;
import ru.yandex.practicum.generated.model.dto.ProductDto;
import ru.yandex.practicum.model.entity.PaymentEntity;
import ru.yandex.practicum.model.enumerated.PaymentStatus;
import ru.yandex.practicum.model.exception.NotFoundException;
import ru.yandex.practicum.repository.PaymentRepository;
import ru.yandex.practicum.service.PaymentService;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderClient orderClient;
    private final PaymentRepository repository;
    private final ShoppingStoreClient shoppingStoreClient;

    @Override
    public BigDecimal getTotalCost(OrderDto orderDto) {
        var ids = orderDto.getProducts().keySet();
        return getProductsCost(getProducts(ids));
    }

    @Override
    @Transactional
    public PaymentDto payment(OrderDto orderDto) {
        var entity = new PaymentEntity();
        entity.setStatus(PaymentStatus.PENDING);
        entity.setOrderId(orderDto.getOrderId());

        var savedEntity = repository.save(entity);

        return new PaymentDto()
                .paymentId(savedEntity.getPaymentId());
    }

    @Override
    public void paymentFailed(UUID body) {
        var entity = findByOrderId(body);
        entity.setStatus(PaymentStatus.FAILED);
        repository.save(entity);
        orderClient.paymentFailed(body);
    }

    @Override
    @Transactional
    public void paymentSuccess(UUID body) {
        var entity = findByOrderId(body);
        entity.setStatus(PaymentStatus.SUCCESS);
        repository.save(entity);
        orderClient.payment(body);
    }

    //   a. От суммы стоимости всех товаров нужно взять 10% — это будет НДС. Например, если товар стоит 100 рублей, то НДС составит 10 рублей.
    //   b. Налог прибавляем к стоимости товара, получим 110 рублей.
    //   c. Добавляем стоимость доставки — 50 рублей. И в итоге пользователь видит сумму: 160 рублей.
    @Override
    public BigDecimal productCost(OrderDto orderDto) {
        final var deliveryCost = new BigDecimal("50");
        var entity = findById(orderDto.getPaymentId());
        var productIds = orderDto.getProducts().keySet();
        var products = getProducts(productIds);
        var productsCost = getProductsCost(products);
        var nds = productsCost.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);
        var totalPayment = productsCost.add(nds).add(deliveryCost);

        entity.setFeeTotal(nds);
        entity.setTotalPayment(totalPayment);
        entity.setDeliveryTotal(deliveryCost);

        repository.saveAndFlush(entity);

        return totalPayment;
    }


    private BigDecimal getProductsCost(List<ProductDto> products) {
        return products.stream()
                .map(ProductDto::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<ProductDto> getProducts(Set<String> productIds) {
        return productIds.stream()
                .map(id -> Objects.requireNonNull(shoppingStoreClient.getProduct(UUID.fromString(id)).getBody()))
                .toList();
    }

    private PaymentEntity findById(UUID paymentId) {
        return repository.findById(paymentId).orElseThrow(() -> {
            var data = new NoOrderFoundException();
            data.setHttpStatus(HttpStatusEnum._404_NOT_FOUND);
            data.setMessage("Заказ не найден");
            return new NotFoundException(data);
        });
    }

    private PaymentEntity findByOrderId(UUID orderId) {
        return repository.findByOrderId(orderId).orElseThrow(() -> {
            var data = new NoOrderFoundException();
            data.setHttpStatus(HttpStatusEnum._404_NOT_FOUND);
            data.setMessage("Заказ не найден");
            return new NotFoundException(data);
        });
    }
}
