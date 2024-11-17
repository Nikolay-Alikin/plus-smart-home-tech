create table orders
(
    order_id         uuid primary key,
    username         varchar(1000) not null,
    state            varchar       not null,
    shopping_cart_id uuid          not null,
    payment_id       uuid          not null,
    delivery_id      uuid          not null,
    delivery_weight  numeric       not null,
    delivery_volume  numeric       not null,
    fragile          boolean       not null,
    total_price      numeric       not null,
    delivery_price   numeric       not null,
    product_price    numeric       not null
);

create table products
(
    id              uuid primary key,
    quantity        bigint not null,
    order_entity_it uuid references orders
)