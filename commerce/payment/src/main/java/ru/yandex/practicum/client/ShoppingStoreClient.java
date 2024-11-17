package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.generated.api.store.ApiStore;

@FeignClient("shopping-store")
public interface ShoppingStoreClient extends ApiStore {

}
