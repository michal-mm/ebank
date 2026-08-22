package airhacks.ebank.transactions.entity;

import java.math.BigDecimal;

import airhacks.ebank.transactions.boundary.TransactionCarrier;

/// Closed set of transaction kinds. [#from(TransactionCarrier)] is the single
/// place where the wire format becomes a domain transaction; sealing forces
/// the processor to handle every kind.
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