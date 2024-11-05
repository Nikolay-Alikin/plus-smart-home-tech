create table if not exists shopping_carts
(
    shopping_cart_id uuid primary key,
    username         varchar(1000) not null
);

create table if not exists products
(
    id               uuid primary key,
    count            int  not null,
    shopping_cart_id uuid not null references shopping_carts
)