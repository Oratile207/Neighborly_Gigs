package co.za.neighborlygigs.service;

import co.za.neighborlygigs.domain.Task;
import co.za.neighborlygigs.domain.Transaction;

public interface TransactionService {
    Transaction createTransaction(Task task);
    // Later: void processPayment(Long transactionId);
}