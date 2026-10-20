package airhacks.ebank.HEX_core.ports.in;

import airhacks.ebank.HEX_core.domain.Transaction;
import airhacks.ebank.HEX_core.domain.Account;
import airhacks.ebank.HEX_adapter.in.rest.transaction.Requirement;
import jakarta.ws.rs.core.Response;

import static airhacks.ebank.HEX_adapter.in.rest.transaction.Requirement.Rn.*;

public interface TransactionInPort {

    @Requirement({R1_1, R1_2, R1_3, R1_4})
    Response processTransaction(String iban, Transaction transaction);

    Account applyTransaction(Account account, Transaction transaction);
}
