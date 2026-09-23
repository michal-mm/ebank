package airhacks.ebank.HEX_core.usecase;

import airhacks.ebank.HEX_core.domain.Transaction;
import airhacks.ebank.HEX_core.ports.in.TransactionInPort;
import airhacks.ebank.HEX_shared.Responses;
import airhacks.ebank.transactions.control.TransactionProcessor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

/// Orchestrates the transaction use case: delegates to [TransactionProcessor]
/// and maps the optional result to an HTTP response. No wire-format concerns
/// live here — the DTO-to-domain mapping happens in the adapter before this
/// port is called.
@ApplicationScoped
public class TransactionsManagementUc implements TransactionInPort {

    @Inject
    TransactionProcessor processor;

    @Override
    public Response processTransaction(String iban, Transaction transaction) {
        return processor
                .processTransaction(iban, transaction)
                .map(Responses::ok)
                .orElseGet(Responses::noContent);
    }
}
