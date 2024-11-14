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
import ru.yandex.practicum.generated.model.dto.AddressDto;
import ru.yandex.practicum.generated.model.dto.AssemblyProductForOrderFromShoppingCartRequest;
import ru.yandex.practicum.generated.model.dto.CreateNewOrderRequest;
import ru.yandex.practicum.generated.model.dto.DeliveryDto;
import ru.yandex.practicum.generated.model.dto.NoOrderFoundException;
import ru.yandex.practicum.generated.model.dto.NoOrderFoundException.HttpStatusEnum;
import ru.yandex.practicum.generated.model.dto.OrderDto;
import ru.yandex.practicum.generated.model.dto.OrderDto.StateEnum;
import ru.yandex.practicum.generated.model.dto.ProductReturnRequest;
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

        var response = warehouseClient.assemblyProductForOrderFromShoppingCart(request);

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
        var deliveryCost = deliveryClient.deliveryCost(dto);
        entity.setDeliveryPrice(deliveryCost);
        orderRepository.save(entity);
        return null;
    }

    @Override
    @Transactional
    public OrderDto calculateTotalCost(UUID body) {
        var entity = findById(body);
        var dto = buildOrderDto(entity);
        var totalCost = paymentClient.getTotalCost(dto);

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
        var warehouseAddress = warehouseClient.getWarehouseAddress();
        var deliveryAddress = buildAddressDto(addressRepository.findByOrderEntity(entity));

        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setOrderId(entity.getOrderId());
        deliveryDto.setFromAddress(warehouseAddress);
        deliveryDto.setToAddress(deliveryAddress);
        var answer = deliveryClient.planDelivery(deliveryDto);

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

    private AddressDto buildAddressDto(AddressEntity addressEntity) {
        var dto = new AddressDto();
        dto.setCountry(addressEntity.getCountry());
        dto.setCity(addressEntity.getCity());
        dto.setStreet(addressEntity.getStreet());
        dto.setHouse(addressEntity.getHouse());
        dto.setFlat(addressEntity.getFlat());
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
