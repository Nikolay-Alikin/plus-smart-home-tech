create table products
(
    product_id       uuid primary key,
    product_name     varchar,
    description      varchar,
    image_src        varchar,
    quantity_state   varchar,
    product_state    varchar,
    rating           decimal,
    product_category varchar,
    price            decimal
);