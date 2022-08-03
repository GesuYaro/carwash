CREATE TABLE IF NOT EXISTS carbox (
    id int NOT NULL PRIMARY KEY,
    opening_time time,
    closing_time time,
    time_coefficient float
);

CREATE TABLE IF NOT EXISTS entry (
    id int NOT NULL PRIMARY KEY,
    carbox_id int,
    service_id int,
    date timestamp,
    status varchar,
    price int
);

CREATE TABLE IF NOT EXISTS carwash_service (
    id int NOT NULL PRIMARY KEY,
    name varchar,
    duration interval,
    price int
);

CREATE SEQUENCE IF NOT EXISTS carbox_seq START 1;
CREATE SEQUENCE IF NOT EXISTS entry_seq START 1;
CREATE SEQUENCE IF NOT EXISTS service_seq START 1;