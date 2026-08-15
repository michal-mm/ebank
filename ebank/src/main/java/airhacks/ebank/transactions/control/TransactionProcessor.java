
package airhacks.ebank.transactions.control;

import java.util.Optional;

import airhacks.ebank.Control;
import airhacks.ebank.accounting.control.AccountFinder;
import airhacks.ebank.accounting.entity.Account;
import airhacks.ebank.transactions.entity.Transaction;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/// Applies a transaction to the account behind an IBAN. Debits are applied
/// unconditionally — the balance may go negative (R1.3); an unknown account
/// yields absence, not an error (R1.4).
@Control
public class TransactionProcessor {
    @PersistenceContext
    EntityManager em;

    @Inject
    AccountFinder finder;

    public Optional<Account> processTransaction(String iban, Transaction transaction) {
        return this.finder.account(iban)
                .map(a -> this.applyTransaction(a, transaction))
                .map(this.em::merge);

    }

    Account applyTransaction(Account account, Transaction transaction) {
        return switch (transaction) {
            case Transaction.Debit debit -> account.debit(debit.amount());
            case Transaction.Deposit deposit -> account.deposit(deposit.amount());
        };

    }
}