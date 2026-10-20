package airhacks.ebank.HEX_adapter.in.rest.accounting;

import static airhacks.ebank.HEX_adapter.in.rest.accounting.Requirement.Rn.*;

import airhacks.ebank.HEX_core.domain.Account;
import airhacks.ebank.HEX_core.ports.in.AccountingInPort;
import airhacks.ebank.HEX_shared.Responses;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/// HTTP entry point for the account lifecycle: initial creation and lookup by
/// IBAN. All decisions are delegated to [AccountingInPort]; the exhaustive
/// switch over the creation result forces every outcome to be mapped to an
/// HTTP status.
@Path("accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Accounts {

    @Inject
    AccountingInPort inPort;

    @GET
    @Path("{iban}")
    @Requirement({R2_1, R2_2})
    public Response account(@PathParam("iban") String iban) {
        return inPort.findAccount(iban)
                .map(AccountResponse::from)
                .map(Responses::ok)
                .orElseGet(Responses::noContent);
    }

    @POST
    @Requirement({R1_1, R1_2, R1_3, R1_4})
    public Response initialCreation(Account account) {
        return inPort.initialCreation(account);
    }
}
