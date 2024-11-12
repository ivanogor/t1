-- liquibase formatted sql

-- changeset ivanogor:172647639227-1
ALTER TABLE transactions
    ADD COLUMN transaction_status VARCHAR(255) NOT NULL,
ADD COLUMN transaction_id UUID NOT NULL UNIQUE;

-- changeset ivanogor:172647639227-2
ALTER TABLE accounts
    ADD COLUMN account_status VARCHAR(255) NOT NULL,
ADD COLUMN account_id UUID NOT NULL UNIQUE,
ADD COLUMN frozen_amount DECIMAL(19, 2);

-- changeset ivanogor:172647639227-3
ALTER TABLE client
    ADD COLUMN client_id UUID NOT NULL UNIQUE;