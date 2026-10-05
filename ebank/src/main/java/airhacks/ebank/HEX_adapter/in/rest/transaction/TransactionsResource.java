package airhacks.ebank.HEX_adapter.in.rest.transaction;

import static airhacks.ebank.HEX_adapter.in.rest.transaction.Requirement.Rn.R1_1;
import static airhacks.ebank.HEX_adapter.in.rest.transaction.Requirement.Rn.R1_2;
import static airhacks.ebank.HEX_adapter.in.rest.transaction.Requirement.Rn.R1_3;
import static airhacks.ebank.HEX_adapter.in.rest.transaction.Requirement.Rn.R1_4;

import airhacks.ebank.Boundary;
import airhacks.ebank.HEX_core.ports.in.TransactionInPort;
import airhacks.ebank.HEX_shared.logging.EBLog;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/// Primary HTTP adapter for the transaction use case. Receives the wire-level
/// [TransactionCarrierDto], maps it to the domain [airhacks.ebank.HEX_core.domain.Transaction] via
/// [TransactionInMapper], and delegates to [TransactionInPort] — the only
/// place in this class that knows about the domain.
@Boundary
@Path("transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionsResource {

    @Inject
    TransactionInPort port;

    @Inject
    EBLog log;

    @POST
    @Path("/{iban}/")
    @Requirement({R1_1, R1_2, R1_3, R1_4})
    public Response processTransaction(@PathParam("iban") String iban, TransactionCarrierDto dto) {
        this.log.info("processTransaction " + iban);
        var transaction = TransactionInMapper.from(dto);
        return port.processTransaction(iban, transaction);
    }
}
