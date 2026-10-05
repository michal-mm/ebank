package airhacks.ebank.HEX_core.ports.in;

import airhacks.ebank.HEX_adapter.out.accounting.Requirement;
import jakarta.ws.rs.core.Response;

import static airhacks.ebank.HEX_adapter.out.accounting.Requirement.Rn.R1_1;
import static airhacks.ebank.HEX_adapter.out.accounting.Requirement.Rn.R1_2;

public interface ReportingInPort {

    @Requirement({R1_1, R1_2})
    Response accounts();
}
