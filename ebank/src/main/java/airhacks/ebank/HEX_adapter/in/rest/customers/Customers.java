package airhacks.ebank.HEX_adapter.in.rest.customers;

import airhacks.ebank.HEX_core.ports.in.CustomersInPort;
import airhacks.ebank.customers.Requirement;
import airhacks.ebank.customers.control.AccountOwnership;
import airhacks.ebank.customers.entity.Customer;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import static airhacks.ebank.customers.Requirement.Rn.*;
import static airhacks.ebank.customers.Requirement.Rn.R2_2;

/// HTTP entry point for the customer lifecycle: registration, lookup, and
/// account ownership. Only the name check (R1.2) happens here; ownership
/// decisions are delegated to [AccountOwnership], whose exhaustive result
/// switch maps every rejection to a distinct HTTP status.
@Path("customers")
public class Customers {

    @Inject
    CustomersInPort customersInPort;

    @POST
    @Requirement({R1_1, R1_2})
    public Response registerCustomer(Customer customer) {
        return customersInPort.registerCustomer(customer);
    }

    @GET
    @Path("{id}")
    @Requirement({R2_1, R2_2})
    public Response fetchCustomer(@PathParam("id") long id) {
        return customersInPort.fetchCustomer(id);
    }

    @POST
    @Path("{id}/accounts/{iban}")
    @Requirement({R3_1, R3_2, R3_3, R3_4, R3_5})
    public Response ownAccount(@PathParam("id") long id, @PathParam("iban") String iban) {
        return customersInPort.ownAccount(id, iban);
    }

    @GET
    @Path("{id}/accounts")
    @Requirement({R4_1, R4_2, R4_3})
    public Response listOwnedAccounts(@PathParam("id") long id) {
        return customersInPort.listOwnedAccounts(id);
    }
}
