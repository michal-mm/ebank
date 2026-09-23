package airhacks.ebank.HEX_adapter.in.rest.accounting;

import airhacks.ebank.HEX_core.ports.in.AccountingInPort;
import airhacks.ebank.accounting.Requirement;
import airhacks.ebank.accounting.control.AccountCreator;
import airhacks.ebank.accounting.control.AccountFinder;
import airhacks.ebank.accounting.entity.Account;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static airhacks.ebank.accounting.Requirement.Rn.*;
import static airhacks.ebank.accounting.Requirement.Rn.R1_2;
import static airhacks.ebank.accounting.Requirement.Rn.R1_3;
import static airhacks.ebank.accounting.Requirement.Rn.R1_4;

/// HTTP entry point for the account lifecycle: initial creation and lookup by
/// IBAN. All decisions are delegated to [AccountCreator] and [AccountFinder];
/// the exhaustive switch over the creation result forces every outcome to be
/// mapped to an HTTP status.
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
        return inPort.account(iban);
    }

    @POST
    @Requirement({R1_1, R1_2, R1_3, R1_4})
    public Response initialCreation(Account account){
        return inPort.initialCreation(account);
    }
}
