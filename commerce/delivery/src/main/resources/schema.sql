create table delivery
(
    delivery_id    uuid        not null,
    order_id       uuid        not null,
    delivery_state varchar(20) not null,
    total_weight   numeric,
    fragile        boolean default false
);

create table addresses
(
    id          uuid        not null,
    country     varchar(20) not null,
    city        varchar(20) not null,
    street      varchar(20) not null,
    house       varchar(20) not null,
    flat        varchar(20) not null,
    delivery_id uuid        not null references delivery,
    is_from     boolean
);