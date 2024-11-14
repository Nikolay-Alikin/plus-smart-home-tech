create table products
(
    product_id uuid primary key,
    fragile    boolean,
    dimension  json    not null,
    weight     numeric not null,
    quantity   bigint
);

create table bookings
(
    id               uuid primary key,
    shopping_cart_id uuid not null,
    product_id       uuid references products,
    quantity         bigint
);

create table orders_deliveries
(
    id          uuid primary key,
    order_id    uuid,
    delivery_id uuid
);