package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.exception.TransactionNotFoundException;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.TransactionService;
import ru.t1.java.demo.util.TransactionMapper;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работы с сущностью Transaction.
 * Предоставляет методы для сохранения, получения, обновления и удаления транзакций.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.2024
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    /**
     * Репозиторий для работы с сущностью Transaction.
     */
    private final TransactionRepository transactionRepository;

    @Override
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        Transaction transaction = TransactionMapper.toEntity(transactionDto);
        Transaction createdTransaction = transactionRepository.save(transaction);
        return TransactionMapper.toDto(createdTransaction);
    }

    @Override
    public TransactionDto getTransaction(Long id) {
        Optional<Transaction> optionalFoundTransaction = transactionRepository.findById(id);
        if (optionalFoundTransaction.isEmpty()) {
            throw new TransactionNotFoundException(id);
        }
        Transaction foundTransaction = optionalFoundTransaction.get();
        return TransactionMapper.toDto(foundTransaction);
    }

    @Override
    public List<TransactionDto> getTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(TransactionMapper::toDto)
                .toList();
    }

    @Override
    public void deleteTransaction(Long id) {
        if(!transactionRepository.existsById(id)) {
            throw new TransactionNotFoundException(id);
        }
        transactionRepository.deleteById(id);
    }

    @Override
    public TransactionDto updateTransaction(Long id, TransactionDto transactionDto) {
        Transaction transactionToUpdate = TransactionMapper.toEntity(transactionDto);
        Optional<Transaction> existingOptionalTransaction = transactionRepository.findById(id);
        if(existingOptionalTransaction.isEmpty()) {
            throw new TransactionNotFoundException(id);
        }
        Transaction existingTransaction = existingOptionalTransaction.get();
        existingTransaction.setAmount(transactionToUpdate.getAmount());

        Transaction updatedTransaction = transactionRepository.save(existingTransaction);
        return TransactionMapper.toDto(updatedTransaction);
    }
}
