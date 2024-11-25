-- liquibase formatted sql

-- changeset ivanogor:172647639223-1
CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 50;

-- changeset ivanogor:172647639223-2
CREATE TABLE users
(
    id       BIGINT PRIMARY KEY DEFAULT nextval('users_seq'),
    username VARCHAR(255) UNIQUE NOT NULL,
    email    VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    role     VARCHAR(50)         NOT NULL
);

