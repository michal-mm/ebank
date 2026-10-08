package airhacks.ebank.HEX_core.usecase;

import airhacks.ebank.HEX_core.domain.Account;
import airhacks.ebank.HEX_core.domain.Transaction;
import airhacks.ebank.HEX_core.ports.in.TransactionInPort;
import airhacks.ebank.HEX_core.ports.out.AccountPersistenceOutPort;
import airhacks.ebank.HEX_shared.Responses;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

/// Orchestrates the transaction use case: finds the account, applies the
/// transaction, and persists the result. No wire-format concerns live here —
/// the DTO-to-domain mapping happens in the adapter before this port is called.
@ApplicationScoped
public class TransactionsManagementUc implements TransactionInPort {

    @Inject
    AccountPersistenceOutPort persistence;

    @Override
    public Response processTransaction(String iban, Transaction transaction) {
        return findAccountAndProcess(iban, transaction)
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

    private Optional<Account> findAccountAndProcess(String iban, Transaction transaction) {
        return persistence.findByIban(iban)
                .map(a -> applyTransaction(a, transaction))
                .map(persistence::merge);
    }
}
