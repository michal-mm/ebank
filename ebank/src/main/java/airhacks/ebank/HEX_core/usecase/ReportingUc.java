package airhacks.ebank.HEX_core.usecase;

import static airhacks.ebank.reporting.Requirement.Rn.R1_1;
import static airhacks.ebank.reporting.Requirement.Rn.R1_2;

import airhacks.ebank.HEX_core.ports.in.ReportingInPort;
import airhacks.ebank.HEX_shared.Responses;
import airhacks.ebank.reporting.Requirement;
import airhacks.ebank.reporting.control.AccountQuery;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;


public class ReportingUc implements ReportingInPort {

    @Inject
    AccountQuery accounts;


    @Requirement({R1_1, R1_2})
    @Override
    public Response accounts() {
        var allAccounts = this.accounts.asIBANs();
        if (allAccounts.isEmpty())
            return Responses.noContent();
        var csv = String.join(",", allAccounts);
        return Responses.ok(csv);
    }
}
