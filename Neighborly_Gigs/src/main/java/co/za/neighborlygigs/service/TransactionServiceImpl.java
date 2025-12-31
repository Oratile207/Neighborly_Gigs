package co.za.neighborlygigs.service;

import co.za.neighborlygigs.domain.Task;
import co.za.neighborlygigs.domain.Transaction;
import co.za.neighborlygigs.factory.TransactionFactory;
import co.za.neighborlygigs.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction createTransaction(Task task) {
        // Check if transaction already exists
        if (transactionRepository.findByTask_Id(task.getId()).isPresent()) {
            throw new RuntimeException("Transaction already exists for this task");
        }

        Transaction transaction = TransactionFactory.createTransaction(task);
        return transactionRepository.save(transaction);
    }
}
