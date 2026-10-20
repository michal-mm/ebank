package airhacks.ebank.HEX_adapter.in.rest.reporting;

import airhacks.ebank.HEX_core.ports.in.ReportingInPort;
import airhacks.ebank.HEX_adapter.out.accounting.Requirement;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static airhacks.ebank.HEX_adapter.out.accounting.Requirement.Rn.R1_1;
import static airhacks.ebank.HEX_adapter.out.accounting.Requirement.Rn.R1_2;

/// Read-only reporting endpoint: renders the IBANs of all accounts as
/// plain-text CSV and never mutates state. No `@Boundary` stereotype — no
/// transaction is needed for pure reads.
@Path("reports")
@Produces(MediaType.TEXT_PLAIN)
public class Reporting {

    @Inject
    ReportingInPort reportingInPort;

    @GET
    @Path("accounts")
    @Requirement({R1_1, R1_2})
    public Response accounts() {
        return reportingInPort.accounts();
    }
}
