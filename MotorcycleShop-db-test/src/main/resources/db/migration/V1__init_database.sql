create table if not exists app_user (
       id bigint not null,
        is_verified bit not null,
        name varchar(255),
        password varchar(255),
        username varchar(255),
        verification_code varchar(255),
        basket_id bigint,
        primary key (id));

create table if not exists app_user_order_carts (
       app_user_id bigint not null,
        order_carts_id bigint not null);

create table if not exists  app_user_roles (
       app_user_id bigint not null,
        roles_id bigint not null);

create table if not exists basket (
       id bigint not null,
        basket_name varchar(255),
        primary key (id));

create table if not exists basket_products (
       basket_id bigint not null,
        products_id bigint not null);

create table if not exists  order_cart (
       id bigint not null,
        basket_name varchar(255),
        city varchar(255),
        first_and_last_name varchar(255),
        order_date timestamp(6),
        phone_number integer,
        postal_code varchar(255),
        street varchar(255),
        username varchar(255),
        primary key (id));

create table if not exists  order_cart_products (
       order_cart_id bigint not null,
        products_id bigint not null);

create table if not exists  product (
       id bigint not null,
        category varchar(255),
        imageurl varchar(255),
        product_description varchar(255),
        product_name varchar(255),
        product_price float(53),
        primary key (id));

create table if not exists product_baskets (
       product_id bigint not null,
        baskets_id bigint not null);

create table if not exists  role (
       id bigint not null,
        name varchar(255),
        primary key (id));
