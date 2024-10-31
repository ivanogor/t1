package ru.t1.java.demo.service;

import ru.t1.java.demo.model.Client;

import java.io.IOException;
import java.util.List;

/**
 * Интерфейс сервиса для работы с сущностью Client.
 * Предоставляет методы для парсинга JSON-файла и преобразования его содержимого в список сущностей Client.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.2024
 */
public interface ClientService {

    /**
     * Парсит JSON-файл и преобразует его содержимое в список сущностей Client.
     *
     * @return список сущностей Client, полученных из JSON-файла
     * @throws IOException если произошла ошибка при чтении или парсинге JSON-файла
     */
    List<Client> parseJson() throws IOException;
}