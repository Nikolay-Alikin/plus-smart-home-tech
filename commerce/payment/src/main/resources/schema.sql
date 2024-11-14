create table payments
(
    payment_id     uuid primary key,
    status         varchar,
    order_id       uuid,
    total_payment  numeric,
    delivery_total numeric,
    fee_total      numeric
)