create schema if not exists inventory_management_service;

create sequence if not exists inventory_management_service.hibernate_sequence START WITH 1;
create sequence if not exists inventory_management_service.default_sequence START WITH 1;

create table if not exists inventory_management_service.users
(
    id                 bigint                   not null primary key,
    deleted            boolean default false    not null,
    enabled            boolean default true     not null,
    created_date_time  timestamp with time zone not null,
    modified_date_time timestamp with time zone,
    first_name         varchar(255)             not null,
    last_name          varchar(255)             not null,
    username           varchar(50) unique       not null,
    password           varchar(255)             not null,
    phone_number       varchar(50) unique       not null,
    email              varchar(255) unique      not null
);

create table if not exists inventory_management_service.roles
(
    id                 bigint                   not null primary key,
    name               varchar(255)             not null,
    created_date_time  timestamp with time zone not null,
    modified_date_time timestamp with time zone,
    default_role       boolean                  not null default false,
    deleted            boolean                  not null default false
);

create table if not exists inventory_management_service.authorities
(
    username  varchar(50),
    authority varchar(255),
    user_id   bigint not null,
    role_id   bigint not null,
    foreign key (user_id) references inventory_management_service.users (id),
    foreign key (role_id) references inventory_management_service.roles (id),
    primary key (user_id, role_id)
);

create table if not exists inventory_management_service.categories
(
    id                 bigint                   not null primary key,
    deleted            boolean                  not null default false,
    created_date_time  timestamp with time zone not null,
    modified_date_time timestamp with time zone,
    name               varchar(255) unique      not null,
    description        varchar(255) unique      not null,
    live_in_market     boolean                  not null default false,
    version            int
);

create table if not exists inventory_management_service.products
(
    id                 bigint                   not null primary key,
    deleted            boolean default false    not null,
    created_date_time  timestamp with time zone not null,
    modified_date_time timestamp with time zone,
    name               varchar(255) unique      not null,
    product_code       varchar(255) unique      not null,
    description        varchar(255) unique      not null,
    quantity           int                      not null,
    current_price      numeric(19, 2)           not null,
    currency           varchar(10)              not null,
    category_id        bigint references inventory_management_service.categories (id)
);

insert into inventory_management_service.users (id, created_date_time, modified_date_time, first_name, last_name,
                                                username,
                                                password, phone_number, email)
select (select nextval('inventory_management_service.default_sequence'::regclass)),
       now(),
       now(),
       'Admin',
       'User',
       'admin_user',
       '$2a$10$31h28pxcjOw3dYFdd9sOwekZFHEgRHx1oQ8GWIGgd0T1uXTInq8Wq',
       '1234567892',
       'admin.user@gmail.com'
where not exists (select 1
                  from inventory_management_service.users
                  where username = 'admin_user');

insert into inventory_management_service.categories (id, created_date_time, modified_date_time, name, description,
                                                     live_in_market, version)
select (select nextval('inventory_management_service.default_sequence'::regclass)),
       now(),
       now(),
       'Electronics Category',
       'Electronics Category Description',
       false,
       0
where not exists (select 1
                  from inventory_management_service.products
                  where name = 'Electronics Category');

insert into inventory_management_service.categories (id, created_date_time, modified_date_time, name, description,
                                                     live_in_market, version)
select (select nextval('inventory_management_service.default_sequence'::regclass)),
       now(),
       now(),
       'Beauty Category',
       'Beauty Category Description',
       false,
       0
where not exists (select 1
                  from inventory_management_service.categories
                  where name = 'Beauty Category');

insert into inventory_management_service.products (id, created_date_time, modified_date_time, name, product_code,
                                                   description, quantity, current_price, currency, category_id)
select (select nextval('inventory_management_service.default_sequence'::regclass)),
       now(),
       now(),
       'IPHONE 13',
       'IPHONE_13',
       'IPHONE 13 APPLE Product',
       10,
       2500,
       'EUR',
       (select c.id from inventory_management_service.categories c where c.name = 'Electronics Category')
where not exists (select 1
                  from inventory_management_service.products
                  where product_code = 'IPHONE_13');

insert into inventory_management_service.products (id, created_date_time, modified_date_time, name, product_code,
                                                   description, quantity, current_price, currency, category_id)
select (select nextval('inventory_management_service.default_sequence'::regclass)),
       now(),
       now(),
       'IPHONE 14',
       'IPHONE_14',
       'IPHONE 14 APPLE Product',
       10,
       2700,
       'EUR',
       (select c.id from inventory_management_service.categories c where c.name = 'Electronics Category')
where not exists (select 1
                  from inventory_management_service.products
                  where product_code = 'IPHONE_14');

insert into inventory_management_service.products (id, created_date_time, modified_date_time, name, product_code,
                                                   description, quantity, current_price, currency)
select (select nextval('inventory_management_service.default_sequence'::regclass)),
       now(),
       now(),
       'Novel',
       'Novel',
       'Novel Product Description',
       10,
       10.0,
       'EUR'
where not exists (select 1
                  from inventory_management_service.products
                  where product_code = 'Novel');

insert into inventory_management_service.products (id, created_date_time, modified_date_time, name, product_code,
                                                   description, quantity, current_price, currency)
select (select nextval('inventory_management_service.default_sequence'::regclass)),
       now(),
       now(),
       'Art',
       'Art',
       'Art Product Description',
       2000,
       6.0,
       'EUR'
where not exists (select 1
                  from inventory_management_service.products
                  where product_code = 'Art');

insert into inventory_management_service.roles (id, name, created_date_time, default_role, deleted)
values ((select nextval('inventory_management_service.default_sequence'::regclass)), 'ADMIN', now(), false, false);

insert into inventory_management_service.roles (id, name, created_date_time, default_role, deleted)
values ((select nextval('inventory_management_service.default_sequence'::regclass)), 'CREATE_PRODUCT', now(), true,
        false);

insert into inventory_management_service.roles (id, name, created_date_time, default_role, deleted)
values ((select nextval('inventory_management_service.default_sequence'::regclass)), 'UPDATE_PRODUCT', now(), true,
        false);

insert into inventory_management_service.roles (id, name, created_date_time, default_role, deleted)
values ((select nextval('inventory_management_service.default_sequence'::regclass)), 'GET_PRODUCT', now(), true, false);

insert into inventory_management_service.roles (id, name, created_date_time, default_role, deleted)
values ((select nextval('inventory_management_service.default_sequence'::regclass)), 'DELETE_PRODUCT', now(), true,
        false);

insert into inventory_management_service.roles (id, name, created_date_time, default_role, deleted)
values ((select nextval('inventory_management_service.default_sequence'::regclass)), 'ADD_PRODUCT', now(), true, false);

insert into inventory_management_service.roles (id, name, created_date_time, default_role, deleted)
values ((select nextval('inventory_management_service.default_sequence'::regclass)), 'REMOVE_PRODUCT', now(), true,
        false);

insert into inventory_management_service.roles (id, name, created_date_time, default_role, deleted)
values ((select nextval('inventory_management_service.default_sequence'::regclass)), 'GET_CATEGORY', now(), true,
        false);

insert into inventory_management_service.roles (id, name, created_date_time, default_role, deleted)
values ((select nextval('inventory_management_service.default_sequence'::regclass)), 'CREATE_CATEGORY', now(), true,
        false);

insert into inventory_management_service.roles (id, name, created_date_time, default_role, deleted)
values ((select nextval('inventory_management_service.default_sequence'::regclass)), 'UPDATE_CATEGORY', now(), true,
        false);

insert into inventory_management_service.roles (id, name, created_date_time, default_role, deleted)
values ((select nextval('inventory_management_service.default_sequence'::regclass)), 'DELETE_CATEGORY', now(), true,
        false);

insert into inventory_management_service.authorities (user_id, role_id, username, authority)
values ((select id from inventory_management_service.users where username = 'admin_user'),
        (select id from inventory_management_service.roles where name = 'ADMIN'), 'admin_user', 'ADMIN');

insert into inventory_management_service.authorities (user_id, role_id, username, authority)
values ((select id from inventory_management_service.users where username = 'admin_user'),
        (select id from inventory_management_service.roles where name = 'CREATE_PRODUCT'), 'admin_user',
        'CREATE_PRODUCT');

insert into inventory_management_service.authorities (user_id, role_id, username, authority)
values ((select id from inventory_management_service.users where username = 'admin_user'),
        (select id from inventory_management_service.roles where name = 'UPDATE_PRODUCT'), 'admin_user',
        'UPDATE_PRODUCT');

insert into inventory_management_service.authorities (user_id, role_id, username, authority)
values ((select id from inventory_management_service.users where username = 'admin_user'),
        (select id from inventory_management_service.roles where name = 'GET_PRODUCT'), 'admin_user',
        'GET_PRODUCT');

insert into inventory_management_service.authorities (user_id, role_id, username, authority)
values ((select id from inventory_management_service.users where username = 'admin_user'),
        (select id from inventory_management_service.roles where name = 'DELETE_PRODUCT'), 'admin_user',
        'DELETE_PRODUCT');

insert into inventory_management_service.authorities (user_id, role_id, username, authority)
values ((select id from inventory_management_service.users where username = 'admin_user'),
        (select id from inventory_management_service.roles where name = 'ADD_PRODUCT'), 'admin_user',
        'ADD_PRODUCT');

insert into inventory_management_service.authorities (user_id, role_id, username, authority)
values ((select id from inventory_management_service.users where username = 'admin_user'),
        (select id from inventory_management_service.roles where name = 'REMOVE_PRODUCT'), 'admin_user',
        'REMOVE_PRODUCT');

insert into inventory_management_service.authorities (user_id, role_id, username, authority)
values ((select id from inventory_management_service.users where username = 'admin_user'),
        (select id from inventory_management_service.roles where name = 'GET_CATEGORY'), 'admin_user',
        'GET_CATEGORY');

insert into inventory_management_service.authorities (user_id, role_id, username, authority)
values ((select id from inventory_management_service.users where username = 'admin_user'),
        (select id from inventory_management_service.roles where name = 'CREATE_CATEGORY'), 'admin_user',
        'CREATE_CATEGORY');

insert into inventory_management_service.authorities (user_id, role_id, username, authority)
values ((select id from inventory_management_service.users where username = 'admin_user'),
        (select id from inventory_management_service.roles where name = 'UPDATE_CATEGORY'), 'admin_user',
        'UPDATE_CATEGORY');

insert into inventory_management_service.authorities (user_id, role_id, username, authority)
values ((select id from inventory_management_service.users where username = 'admin_user'),
        (select id from inventory_management_service.roles where name = 'DELETE_CATEGORY'), 'admin_user',
        'DELETE_CATEGORY');
