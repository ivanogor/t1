-- liquibase formatted sql

-- changeset ivanogor:172647765239-1
CREATE TABLE service2_users
(
    id       BIGINT PRIMARY KEY DEFAULT nextval('users_seq'),
    username VARCHAR(255) UNIQUE NOT NULL,
    email    VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    role     VARCHAR(50)         NOT NULL
);