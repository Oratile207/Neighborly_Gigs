package co.za.neighborlygigs.factory;

import co.za.neighborlygigs.domain.Task;
import co.za.neighborlygigs.domain.Transaction;
import co.za.neighborlygigs.domain.enums.TransactionStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TransactionFactory {
    private static final BigDecimal PLATFORM_FEE_RATE = new BigDecimal("0.20");

    public static Transaction createTransaction(Task task) {
        BigDecimal total = task.getBudget();
        BigDecimal platformFee = total.multiply(PLATFORM_FEE_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal payout = total.subtract(platformFee).setScale(2, RoundingMode.HALF_UP);

        return Transaction.builder()
                .task(task)
                .totalAmount(total)
                .platformFee(platformFee)
                .payoutToCompleter(payout)
                .status(TransactionStatus.PENDING)
                .build();
    }
}
