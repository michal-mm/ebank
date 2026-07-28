package airhacks.ebank.transactions.entity;

import java.math.BigDecimal;

import airhacks.ebank.accounting.boundary.TransactionCarrier;

public sealed interface Transaction permits Transaction.Debit, Transaction.Deposit{
    BigDecimal amount();
    record Debit(BigDecimal amount) implements Transaction {}
    record Deposit(BigDecimal amount) implements Transaction {}

    static Transaction from(TransactionCarrier transaction) {
        var type = transaction.type();
        var amount = transaction.amount();
        return switch (type) {
            case DEBIT -> new Debit(amount);
            case DEPOSIT -> new Deposit(amount);
        };
    }
}