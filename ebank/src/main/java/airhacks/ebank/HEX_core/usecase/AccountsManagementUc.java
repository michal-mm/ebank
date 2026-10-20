package airhacks.ebank.HEX_core.usecase;

import static airhacks.ebank.HEX_adapter.in.rest.accounting.Requirement.Rn.R1_1;
import static airhacks.ebank.HEX_adapter.in.rest.accounting.Requirement.Rn.R1_2;
import static airhacks.ebank.HEX_adapter.in.rest.accounting.Requirement.Rn.R1_3;
import static airhacks.ebank.HEX_adapter.in.rest.accounting.Requirement.Rn.R1_4;
import static airhacks.ebank.HEX_adapter.in.rest.accounting.Requirement.Rn.R2_1;
import static airhacks.ebank.HEX_adapter.in.rest.accounting.Requirement.Rn.R2_2;

import airhacks.ebank.Boundary;
import airhacks.ebank.HEX_core.domain.Account;
import airhacks.ebank.HEX_core.domain.AccountCreationResult;
import airhacks.ebank.HEX_core.domain.AccountCreationResult.AlreadyExists;
import airhacks.ebank.HEX_core.domain.AccountCreationResult.Created;
import airhacks.ebank.HEX_core.domain.AccountCreationResult.Invalid;
import airhacks.ebank.HEX_core.domain.CreationResponses;
import airhacks.ebank.HEX_core.ports.in.AccountingInPort;
import airhacks.ebank.HEX_core.ports.out.AccountPersistenceOutPort;
import airhacks.ebank.HEX_shared.Responses;
import airhacks.ebank.HEX_shared.logging.EBLog;
import airhacks.ebank.HEX_adapter.in.rest.accounting.Requirement;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

/// Orchestrates the account lifecycle: creation and lookup by IBAN.
/// Persistence is delegated to [AccountPersistenceOutPort]; no JPA or
/// infrastructure concern lives here.
@Boundary
@Path("accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountsManagementUc implements AccountingInPort {

    @Inject
    AccountPersistenceOutPort persistence;

    @Inject
    EBLog log;

    @Override
    public Optional<Account> findAccount(String iban) {
        return persistence.findByIban(iban);
    }

    @GET
    @Path("{iban}")
    @Requirement({R2_1, R2_2})
    public Response account(@PathParam("iban") String iban) {
        this.log.info("get account " + iban);
        return persistence.findByIban(iban)
                .map(Responses::ok)
                .orElseGet(Responses::noContent);
    }

    @POST
    @Requirement({R1_1, R1_2, R1_3, R1_4})
    public Response initialCreation(Account account) {
        this.log.info("initialCreation " + account);
        var result = initialAccountCreation(account);
        return switch (result) {
            case Created created -> CreationResponses.created(created);
            case AlreadyExists exists -> CreationResponses.alreadyExists(exists);
            case Invalid invalid -> CreationResponses.invalid(invalid);
        };
    }

    @Override
    public AccountCreationResult initialAccountCreation(Account account) {
        if (!isValidForCreation(account))
            return new AccountCreationResult.Invalid(account);
        if (persistence.existsByIban(account.iban()))
            return new AccountCreationResult.AlreadyExists(account);
        persistence.save(account);
        return new AccountCreationResult.Created(account);
    }

    boolean isValidForCreation(Account account) {
        return account.isBalancePositive()
                && (account.balance() < 1000);
    }
}
