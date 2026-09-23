package airhacks.ebank.HEX_core.ports.in;

import airhacks.ebank.reporting.Requirement;
import jakarta.ws.rs.core.Response;

import static airhacks.ebank.reporting.Requirement.Rn.R1_1;
import static airhacks.ebank.reporting.Requirement.Rn.R1_2;

public interface ReportingInPort {

    @Requirement({R1_1, R1_2})
    Response accounts();
}
