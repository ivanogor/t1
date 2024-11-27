-- liquibase formatted sql

-- changeset ivanogor:1726471197331-1
ALTER TABLE client
ADD COLUMN client_status VARCHAR(255) NOT NULL;