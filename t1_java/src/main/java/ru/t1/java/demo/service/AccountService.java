package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.model.Account;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс сервиса для работы с сущностью Account.
 * Предоставляет методы для сохранения, получения, обновления и удаления банковских счетов.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.2024
 */
public interface AccountService {

    /**
     * Сохраняет новый банковский счет в системе.
     *
     * @param accountDto банковский счет для сохранения
     * @return сохраненный банковский счет
     */
    Account createAccount(AccountDto accountDto);

    /**
     * Получает банковский счет по его идентификатору.
     *
     * @param id идентификатор банковского счета
     * @return Optional, содержащий банковский счет, если он найден, иначе пустой Optional
     */
    Optional<Account> getAccount(Long id);

    /**
     * Получает список всех банковских счетов в системе.
     *
     * @return список всех банковских счетов
     */
    List<Account> getAccounts();

    /**
     * Удаляет банковский счет по его идентификатору.
     *
     * @param id идентификатор банковского счета для удаления
     */
    void deleteAccount(Long id);

    /**
     * Обновляет информацию о банковском счете.
     *
     * @param updatedAccountDto банковский счет с обновленными данными
     * @param id идентификатор аккаунта
     * @return обновленный банковский счет
     */
    Account updateAccount(Long id, AccountDto updatedAccountDto);

    /**
     * Парсит JSON-файл и преобразует его содержимое в список сущностей Account.
     *
     * @return список сущностей Account, полученных из JSON-файла
     * @throws IOException если произошла ошибка при чтении или парсинге JSON-файла
     */
    List<Account> parseJson() throws IOException;
}