package airhacks.ebank.HEX_core.ports.in;

import airhacks.ebank.accounting.Requirement;
import airhacks.ebank.accounting.entity.Account;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import static airhacks.ebank.accounting.Requirement.Rn.*;
import static airhacks.ebank.accounting.Requirement.Rn.R1_2;
import static airhacks.ebank.accounting.Requirement.Rn.R1_3;
import static airhacks.ebank.accounting.Requirement.Rn.R1_4;

public interface AccountingInPort {

    @Requirement({R2_1, R2_2})
    Response account(@PathParam("iban") String iban);

    @Requirement({R1_1, R1_2, R1_3, R1_4})
    Response initialCreation(Account account);
}
