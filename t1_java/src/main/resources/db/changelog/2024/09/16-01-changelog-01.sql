-- liquibase formatted sql

-- changeset e_cha:1726476397331-1
CREATE SEQUENCE IF NOT EXISTS client_seq START WITH 1 INCREMENT BY 50;

-- changeset e_cha:1726476397331-2
CREATE TABLE client
(
    id BIGINT NOT NULL,
    CONSTRAINT pk_client PRIMARY KEY (id)
);

-- changeset ivanogor:1726476397331-3
CREATE SEQUENCE IF NOT EXISTS accounts_seq START WITH 1 INCREMENT BY 50;

-- changeset ivanogor:1726476397331-4
CREATE TABLE accounts
(
    id BIGINT PRIMARY KEY DEFAULT nextval('accounts_seq'),
    account_type VARCHAR(255) NOT NULL,
    balance DECIMAL(19, 2) NOT NULL,
    client_id BIGINT,
    FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE
);

-- changeset ivanogor:1726476397331-5
CREATE SEQUENCE IF NOT EXISTS transactions_seq START WITH 1 INCREMENT BY 50;

-- changeset ivanogor:1726476397331-6
CREATE TABLE transactions (
    id BIGINT PRIMARY KEY DEFAULT nextval('transactions_seq'),
    amount DECIMAL(19, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    account_id BIGINT,
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);

-- changeset ivanogor:1726476397331-7
CREATE SEQUENCE IF NOT EXISTS data_source_error_logs_seq START WITH 1 INCREMENT BY 50;

-- changeset ivanogor:1726476397331-8
CREATE TABLE data_source_error_logs (
                                        id BIGINT PRIMARY KEY DEFAULT nextval('data_source_error_logs_seq'),
                                        stack_trace TEXT NOT NULL,
                                        message TEXT NOT NULL,
                                        method_signature TEXT NOT NULL
);