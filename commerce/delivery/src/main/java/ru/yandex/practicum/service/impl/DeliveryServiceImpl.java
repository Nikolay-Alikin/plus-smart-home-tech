package ru.yandex.practicum.service.impl;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.generated.model.delivery.dto.NoOrderFoundException;
import ru.yandex.practicum.generated.model.delivery.dto.NoOrderFoundException.HttpStatusEnum;
import ru.yandex.practicum.generated.model.warehouse.dto.ShippedToDeliveryRequest;
import ru.yandex.practicum.generated.model.delivery.dto.OrderDto;
import ru.yandex.practicum.generated.model.delivery.dto.AddressDto;
import ru.yandex.practicum.generated.model.delivery.dto.DeliveryState;
import ru.yandex.practicum.generated.model.delivery.dto.DeliveryDto;
import ru.yandex.practicum.model.entity.AddressEntity;
import ru.yandex.practicum.model.entity.DeliveryEntity;
import ru.yandex.practicum.model.exception.NotFoundException;
import ru.yandex.practicum.repository.AddressRepository;
import ru.yandex.practicum.repository.DeliveryRepository;
import ru.yandex.practicum.service.DeliveryService;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final AddressRepository addressRepository;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    @Override
    @Transactional
    public BigDecimal deliveryCost(OrderDto orderDto) {
        var warehouseAddress = findWarehouse(orderDto.getDeliveryId());
        var deliveryAddress = findDeliveryAddress(orderDto.getDeliveryId());
        var deliveryEntity = findById(orderDto.getDeliveryId());

        var warehouseStreet = warehouseAddress.getStreet();
        var deliveryStreet = deliveryAddress.getStreet();

        var baseCost = new BigDecimal(5);
        if (warehouseStreet.equals("ADDRESS_2")) {
            baseCost = baseCost.multiply(new BigDecimal(2));
        }
        BigDecimal withCostByAddress;
        if (orderDto.getFragile()) {
            withCostByAddress = baseCost.multiply(new BigDecimal("0.2")).add(baseCost);
        } else {
            withCostByAddress = baseCost;
        }
        var totalWeight = BigDecimal.valueOf(orderDto.getDeliveryWeight());
        var withCostByWeight = totalWeight.multiply(new BigDecimal("0.3"))
                .add(withCostByAddress);
        var withCostByVolume = BigDecimal.valueOf(orderDto.getDeliveryVolume()).multiply(new BigDecimal("0.2"))
                .add(withCostByWeight);

        BigDecimal totalCost;
        if (warehouseStreet.equals(deliveryStreet)) {
            totalCost = withCostByVolume;
        } else {
            totalCost = withCostByVolume.multiply(new BigDecimal("0.2")).add(withCostByVolume);
        }

        deliveryEntity.setFragile(orderDto.getFragile());
        deliveryEntity.setTotalWeight(totalWeight);

        deliveryRepository.save(deliveryEntity);

        return totalCost;
    }


    @Override
    @Transactional
    public void deliveryFailed(UUID body) {
        var entity = findByOrderId(body);
        entity.setDeliveryState(DeliveryState.FAILED);
        deliveryRepository.save(entity);
    }

    @Override
    @Transactional
    public void deliveryPicked(UUID body) {
        var entity = findByOrderId(body);
        entity.setDeliveryState(DeliveryState.IN_PROGRESS);
        var orderDto = orderClient.assembly(body).getBody();
        var request = new ShippedToDeliveryRequest()
                .deliveryId(entity.getDeliveryId())
                .orderId(orderDto.getOrderId());
        warehouseClient.shippedToDelivery(request);
        deliveryRepository.save(entity);
    }

    @Override
    @Transactional
    public void deliverySuccessful(UUID body) {
        var entity = findByOrderId(body);
        entity.setDeliveryState(DeliveryState.DELIVERED);
        deliveryRepository.save(entity);
    }

    @Override
    @Transactional
    public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
        var deliveryEntity = buildDeliveryEntity(deliveryDto);
        var deliveryEntityAfterSave = deliveryRepository.save(deliveryEntity);
        var fromAddressEntity = buildAddressEntity(deliveryDto.getFromAddress(), deliveryEntityAfterSave, true);
        var toAddressEntity = buildAddressEntity(deliveryDto.getToAddress(), deliveryEntityAfterSave, false);

        addressRepository.save(fromAddressEntity);
        addressRepository.save(toAddressEntity);

        deliveryDto.setDeliveryId(deliveryEntityAfterSave.getDeliveryId());
        deliveryDto.setDeliveryState(DeliveryState.valueOf(
                deliveryEntityAfterSave.getDeliveryState().name()));
        return deliveryDto;
    }

    private AddressEntity buildAddressEntity(AddressDto addressDto,
            DeliveryEntity deliveryEntity, boolean isFrom) {
        var addressEntity = new AddressEntity();
        addressEntity.setCountry(addressDto.getCountry());
        addressEntity.setCity(addressDto.getCity());
        addressEntity.setStreet(addressDto.getStreet());
        addressEntity.setHouse(addressDto.getHouse());
        addressEntity.setFlat(addressDto.getFlat());
        addressEntity.setDeliveryEntity(deliveryEntity);
        if (isFrom) {
            addressEntity.setFrom(true);
        }
        return addressEntity;
    }

    private DeliveryEntity buildDeliveryEntity(DeliveryDto deliveryDto) {
        var deliveryEntity = new DeliveryEntity();
        deliveryEntity.setOrderId(deliveryDto.getOrderId());
        deliveryEntity.setDeliveryState(DeliveryState.CREATED);
        return deliveryEntity;
    }

    private DeliveryEntity findByOrderId(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId).orElseThrow(() -> {
            var data = new NoOrderFoundException();
            data.setHttpStatus(HttpStatusEnum._404_NOT_FOUND);
            data.setMessage("Заказ не найден");
            return new NotFoundException(data);
        });
    }

    private AddressEntity findWarehouse(UUID deliveryId) {
        return addressRepository.findByDeliveryEntityDeliveryIdAndFromIsTrue(deliveryId).orElseThrow(() -> {
            NoOrderFoundException data = new NoOrderFoundException();
            data.setHttpStatus(HttpStatusEnum._404_NOT_FOUND);
            data.setMessage("Не найдена доставка для расчёта");
            return new NotFoundException(data);
        });
    }

    private AddressEntity findDeliveryAddress(UUID deliveryId) {
        return addressRepository.findByDeliveryEntityDeliveryIdAndFromIsFalse(deliveryId).orElseThrow(() -> {
            NoOrderFoundException data = new NoOrderFoundException();
            data.setHttpStatus(HttpStatusEnum._404_NOT_FOUND);
            data.setMessage("Не найдена доставка для расчёта");
            return new NotFoundException(data);
        });
    }

    private DeliveryEntity findById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId).orElseThrow(() -> {
            NoOrderFoundException data = new NoOrderFoundException();
            data.setHttpStatus(HttpStatusEnum._404_NOT_FOUND);
            data.setMessage("Не найдена доставка для расчёта");
            return new NotFoundException(data);
        });
    }
}
