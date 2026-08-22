package airhacks.ebank.transactions.boundary;

import java.math.BigDecimal;

/// Wire representation of a transaction submission. Keeps the JSON contract
/// independent of the sealed `Transaction` domain type, which is derived
/// from this carrier.
public record TransactionCarrier(TransactionType type, BigDecimal amount) {
    public enum TransactionType{
        DEPOSIT, DEBIT
    }

}
