package ru.yandex.practicum.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.client.DeliveryClient;
import ru.yandex.practicum.client.PaymentClient;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.exception.NoOrderFound;
import ru.yandex.practicum.generated.model.oder.dto.AddressDto;
import ru.yandex.practicum.generated.model.oder.dto.CreateNewOrderRequest;
import ru.yandex.practicum.generated.model.oder.dto.DeliveryDto;
import ru.yandex.practicum.generated.model.oder.dto.NoOrderFoundException;
import ru.yandex.practicum.generated.model.oder.dto.NoOrderFoundException.HttpStatusEnum;
import ru.yandex.practicum.generated.model.oder.dto.OrderDto;
import ru.yandex.practicum.generated.model.oder.dto.OrderDto.StateEnum;
import ru.yandex.practicum.generated.model.oder.dto.ProductReturnRequest;
import ru.yandex.practicum.generated.model.warehouse.dto.AssemblyProductForOrderFromShoppingCartRequest;
import ru.yandex.practicum.model.entity.AddressEntity;
import ru.yandex.practicum.model.entity.OrderEntity;
import ru.yandex.practicum.model.entity.ProductEntity;
import ru.yandex.practicum.model.enumerated.State;
import ru.yandex.practicum.repository.AddressRepository;
import ru.yandex.practicum.repository.OrderRepository;
import ru.yandex.practicum.service.OrderService;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final PaymentClient paymentClient;
    private final WarehouseClient warehouseClient;
    private final OrderRepository orderRepository;
    private final DeliveryClient deliveryClient;
    private final AddressRepository addressRepository;

    @Override
    public OrderDto assembly(UUID body) {
        var orderEntity = findById(body);
        var request = new AssemblyProductForOrderFromShoppingCartRequest();

        request.setOrderId(body);
        request.setShoppingCartId(orderEntity.getShoppingCartId());

        var response = warehouseClient.assemblyProductForOrderFromShoppingCart(request).getBody();

        orderEntity.setState(State.ASSEMBLED);
        orderEntity.setFragile(response.getFragile());
        orderEntity.setDeliveryVolume(response.getDeliveryVolume());
        orderEntity.setDeliveryWeight(response.getDeliveryWeight());

        var entityAfterSave = orderRepository.saveAndFlush(orderEntity);

        return buildOrderDto(entityAfterSave);
    }

    @Override
    public OrderDto assemblyFailed(UUID body) {
        var orderEntity = findById(body);
        orderEntity.setState(State.ASSEMBLY_FAILED);
        orderRepository.saveAndFlush(orderEntity);

        return buildOrderDto(orderEntity);
    }

    @Override
    @Transactional
    public OrderDto calculateDeliveryCost(UUID body) {
        var entity = findById(body);
        var dto = buildOrderDto(entity);
        var deliveryCost = deliveryClient.deliveryCost(dto).getBody();
        entity.setDeliveryPrice(deliveryCost);
        orderRepository.save(entity);
        dto.setDeliveryPrice(deliveryCost);

        return dto;
    }

    @Override
    @Transactional
    public OrderDto calculateTotalCost(UUID body) {
        var entity = findById(body);
        var dto = buildOrderDto(entity);
        var totalCost = paymentClient.getTotalCost(dto).getBody();

        entity.setTotalPrice(totalCost);
        dto.setTotalPrice(totalCost);

        orderRepository.save(entity);
        return dto;
    }

    @Override
    public OrderDto complete(UUID body) {
        var orderEntity = findById(body);
        orderEntity.setState(State.COMPLETED);
        orderRepository.saveAndFlush(orderEntity);

        return buildOrderDto(orderEntity);
    }

    @Override
    public OrderDto createNewOrder(CreateNewOrderRequest createNewOrderRequest) {
        var orderEntity = new OrderEntity();
        var shoppingCart = createNewOrderRequest.getShoppingCart();
        var deliveryAddress = createNewOrderRequest.getDeliveryAddress();

        orderEntity.setState(State.NEW);
        orderEntity.setShoppingCartId(shoppingCart.getShoppingCartId());
        orderEntity.setProducts(shoppingCart.getProducts()
                .entrySet()
                .stream()
                .map(e -> {
                    var product = new ProductEntity();
                    product.setId(UUID.fromString(e.getKey()));
                    product.setOrderEntity(orderEntity);
                    product.setQuantity(e.getValue());
                    return product;
                })
                .toList());

        var address = new AddressEntity();
        address.setCity(deliveryAddress.getCity());
        address.setStreet(deliveryAddress.getStreet());
        address.setHouse(deliveryAddress.getHouse());
        address.setFlat(deliveryAddress.getFlat());
        orderEntity.getAddresses().add(address);

        var savedEntity = orderRepository.saveAndFlush(orderEntity);

        return buildOrderDto(savedEntity);
    }

    @Override
    @Transactional
    public OrderDto delivery(UUID body) {
        var entity = findById(body);
        var warehouseAddress = warehouseClient.getWarehouseAddress().getBody();
        var deliveryAddress = buildAddressDto(addressRepository.findByOrderEntity(entity));

        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setOrderId(entity.getOrderId());
        deliveryDto.setFromAddress(buildAddressDto(warehouseAddress));
        deliveryDto.setToAddress(deliveryAddress);
        var answer = deliveryClient.planDelivery(deliveryDto).getBody();

        entity.setDeliveryId(answer.getDeliveryId());

        orderRepository.save(entity);
        return buildOrderDto(entity);
    }

    @Override
    public OrderDto deliveryFailed(UUID body) {
        var orderEntity = findById(body);
        orderEntity.setState(State.DELIVERY_FAILED);
        orderRepository.saveAndFlush(orderEntity);

        return buildOrderDto(orderEntity);
    }

    @Override
    public List<OrderDto> getClientOrders(String username) {
        return findAllByUsername(username)
                .stream()
                .map(this::buildOrderDto)
                .toList();
    }

    @Override
    public OrderDto payment(UUID body) {
        var orderEntity = findById(body);
        orderEntity.setState(State.PAID);
        orderRepository.saveAndFlush(orderEntity);

        return buildOrderDto(orderEntity);
    }

    @Override
    public OrderDto paymentFailed(UUID body) {
        var orderEntity = findById(body);
        orderEntity.setState(State.PAYMENT_FAILED);
        orderRepository.saveAndFlush(orderEntity);

        return buildOrderDto(orderEntity);
    }

    @Override
    public OrderDto productReturn(ProductReturnRequest productReturnRequest) {
        return null;
    }

    private <T> AddressDto buildAddressDto(T address) {
        var dto = new AddressDto();
        if (address instanceof AddressEntity entity) {
            dto.setCountry(entity.getCountry());
            dto.setCity(entity.getCity());
            dto.setStreet(entity.getStreet());
            dto.setHouse(entity.getHouse());
            dto.setFlat(entity.getFlat());
        } else if (address instanceof ru.yandex.practicum.generated.model.warehouse.dto.AddressDto warehouseDto) {
            dto.setCountry(warehouseDto.getCountry());
            dto.setCity(warehouseDto.getCity());
            dto.setStreet(warehouseDto.getStreet());
            dto.setHouse(warehouseDto.getHouse());
            dto.setFlat(warehouseDto.getFlat());
        }
        return dto;
    }

    private OrderEntity findById(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> {
            var data = new NoOrderFoundException();
            data.setHttpStatus(HttpStatusEnum._400_BAD_REQUEST);
            data.setMessage("Заказ не найден");
            return new NoOrderFound(data);
        });
    }

    private List<OrderEntity> findAllByUsername(String username) {
        return orderRepository.findByUsername(username).orElseThrow(() -> {
            var data = new NoOrderFoundException();
            data.setHttpStatus(HttpStatusEnum._400_BAD_REQUEST);
            data.setMessage("Заказ не найден");
            return new NoOrderFound(data);
        });
    }


    private OrderDto buildOrderDto(OrderEntity orderEntity) {
        var dto = new OrderDto();
        dto.setOrderId(orderEntity.getOrderId());
        dto.setShoppingCartId(JsonNullable.of(orderEntity.getShoppingCartId()));
        dto.setProducts(orderEntity.getProducts().stream().collect(Collectors.toMap(it -> it.getId().toString(),
                ProductEntity::getQuantity)));
        dto.setDeliveryId(orderEntity.getDeliveryId());
        dto.setFragile(orderEntity.isFragile());
        dto.setDeliveryVolume(orderEntity.getDeliveryVolume());
        dto.setPaymentId(orderEntity.getPaymentId());
        dto.setState(StateEnum.valueOf(String.valueOf(orderEntity.getState())));
        dto.setDeliveryWeight(orderEntity.getDeliveryWeight());
        dto.setTotalPrice(orderEntity.getTotalPrice());
        dto.setDeliveryPrice(orderEntity.getDeliveryPrice());
        dto.setProductPrice(orderEntity.getProductPrice());

        return dto;
    }
}