package airhacks.ebank.HEX_core.ports.in;

import static airhacks.ebank.HEX_adapter.in.rest.customers.Requirement.Rn.*;

import airhacks.ebank.HEX_adapter.in.rest.customers.Requirement;
import airhacks.ebank.HEX_core.domain.Customer;
import jakarta.ws.rs.core.Response;

public interface CustomersInPort {

    @Requirement({R1_1, R1_2})
    Response registerCustomer(Customer customer);

    @Requirement({R2_1, R2_2})
    Response fetchCustomer(long id);

    @Requirement({R3_1, R3_2, R3_3, R3_4, R3_5})
    Response ownAccount(long id, String iban);

    @Requirement({R4_1, R4_2, R4_3})
    Response listOwnedAccounts(long id);
}
