package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.generated.api.warehouse.ApiWarehouse;

@FeignClient(name = "warehouse")
public interface WarehouseClient extends ApiWarehouse {

}
