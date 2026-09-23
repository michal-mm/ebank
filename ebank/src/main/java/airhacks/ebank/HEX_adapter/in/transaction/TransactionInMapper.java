package airhacks.ebank.HEX_adapter.in.transaction;

import airhacks.ebank.HEX_core.domain.Transaction;

public class TransactionInMapper {

    public static Transaction from(TransactionCarrierDto transaction) {
        var type = transaction.type();
        var amount = transaction.amount();
        return switch (type) {
            case DEBIT -> new Transaction.Debit(amount);
            case DEPOSIT -> new Transaction.Deposit(amount);
        };
    }
}
