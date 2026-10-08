package airhacks.ebank.HEX_core.ports.in;

import airhacks.ebank.HEX_adapter.in.rest.accounting.Requirement;
import airhacks.ebank.HEX_core.domain.Account;
import airhacks.ebank.HEX_core.domain.AccountCreationResult;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

import static airhacks.ebank.HEX_adapter.in.rest.accounting.Requirement.Rn.*;

public interface AccountingInPort {

    @Requirement({R2_1, R2_2})
    Optional<Account> findAccount(String iban);

    @Requirement({R1_1, R1_2, R1_3, R1_4})
    Response initialCreation(Account account);

    AccountCreationResult initialAccountCreation(Account account);
}
