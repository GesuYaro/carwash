CREATE TABLE IF NOT EXISTS carbox (
    id bigint NOT NULL PRIMARY KEY,
    opening_time time,
    closing_time time,
    time_coefficient float
);

CREATE TABLE IF NOT EXISTS entry (
    id bigint NOT NULL PRIMARY KEY,
    carbox_id bigint,
    service_id bigint,
    date timestamp,
    status varchar,
    price bigint,
    user_id bigint
);

CREATE TABLE IF NOT EXISTS carwash_service (
    id bigint NOT NULL PRIMARY KEY,
    name varchar,
    duration bigint,
    price bigint
);

CREATE TABLE IF NOT EXISTS available_interval (
    id        bigint NOT NULL PRIMARY KEY,
    carbox_id bigint,
    from_time bigint,
    until_time bigint
);

CREATE TABLE IF NOT EXISTS app_user (
    id       bigint NOT NULL PRIMARY KEY,
    username varchar unique,
    password varchar,
    role     varchar
);

CREATE TABLE IF NOT EXISTS operator_info (
    id bigint NOT NULL PRIMARY KEY,
    user_id  bigint NOT NULL,
    carbox_id bigint NOT NULL,
    min_sale float,
    max_sale float
);

CREATE SEQUENCE IF NOT EXISTS carbox_seq START 1;
CREATE SEQUENCE IF NOT EXISTS entry_seq START 1;
CREATE SEQUENCE IF NOT EXISTS service_seq START 1;
CREATE SEQUENCE IF NOT EXISTS interval_seq START 1;
CREATE SEQUENCE IF NOT EXISTS user_seq START 1;
CREATE SEQUENCE IF NOT EXISTS op_info_seq START 1;