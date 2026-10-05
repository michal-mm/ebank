package airhacks.ebank.HEX_core.usecase;

import airhacks.ebank.HEX_core.domain.Transaction;
import airhacks.ebank.HEX_core.ports.in.TransactionInPort;
import airhacks.ebank.HEX_shared.Responses;
import airhacks.ebank.accounting.control.AccountFinder;
import airhacks.ebank.accounting.entity.Account;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

/// Orchestrates the transaction use case: find account and process transaction
/// and maps the optional result to an HTTP response. No wire-format concerns
/// live here — the DTO-to-domain mapping happens in the adapter before this
/// port is called.
@ApplicationScoped
public class TransactionsManagementUc implements TransactionInPort {

    @PersistenceContext
    EntityManager em;

    @Inject
    AccountFinder finder;

    @Override
    public Response processTransaction(String iban, Transaction transaction) {
        return findAccountAndProcessTransaction(iban, transaction)
                .map(Responses::ok)
                .orElseGet(Responses::noContent);
    }

    @Override
    public Account applyTransaction(Account account, Transaction transaction) {
        return switch (transaction) {
            case Transaction.Debit debit -> account.debit(debit.amount());
            case Transaction.Deposit deposit -> account.deposit(deposit.amount());
        };
    }

    private Optional<Account> findAccountAndProcessTransaction(String iban, Transaction transaction) {
        return this.finder.account(iban)
                .map(a -> applyTransaction(a, transaction))
                .map(this.em::merge);

    }
}
