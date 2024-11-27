-- liquibase formatted sql

-- changeset ivanogor:172647633227-1
CREATE SEQUENCE IF NOT EXISTS data_source_error_logs_seq START WITH 1 INCREMENT BY 50;

-- changeset ivanogor:172647633227-2
CREATE TABLE data_source_error_logs (
                                        id BIGINT PRIMARY KEY DEFAULT nextval('data_source_error_logs_seq'),
                                        stack_trace TEXT NOT NULL,
                                        message TEXT NOT NULL,
                                        method_signature TEXT NOT NULL
);