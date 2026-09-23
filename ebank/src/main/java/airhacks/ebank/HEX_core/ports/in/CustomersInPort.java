package airhacks.ebank.HEX_core.ports.in;

import airhacks.ebank.accounting.Requirement;
import airhacks.ebank.customers.entity.Customer;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import static airhacks.ebank.accounting.Requirement.Rn.R2_1;
import static airhacks.ebank.accounting.Requirement.Rn.R2_2;
import static airhacks.ebank.customers.Requirement.Rn.*;

public interface CustomersInPort {

    @airhacks.ebank.customers.Requirement({R1_1, R1_2})
    Response registerCustomer(Customer customer);

    @airhacks.ebank.customers.Requirement({airhacks.ebank.customers.Requirement.Rn.R2_1, airhacks.ebank.customers.Requirement.Rn.R2_2})
    Response fetchCustomer(@PathParam("id") long id);

    @airhacks.ebank.customers.Requirement({R3_1, R3_2, R3_3, R3_4, R3_5})
    Response ownAccount(@PathParam("id") long id, @PathParam("iban") String iban);

    @airhacks.ebank.customers.Requirement({R4_1, R4_2, R4_3})
    Response listOwnedAccounts(@PathParam("id") long id);
}
