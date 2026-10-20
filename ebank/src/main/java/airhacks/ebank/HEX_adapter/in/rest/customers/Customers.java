package airhacks.ebank.HEX_adapter.in.rest.customers;

import static airhacks.ebank.HEX_adapter.in.rest.customers.Requirement.Rn.*;

import airhacks.ebank.HEX_core.domain.Customer;
import airhacks.ebank.HEX_core.ports.in.CustomersInPort;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/// Primary HTTP adapter for the customer lifecycle. Delegates every call to
/// [CustomersInPort] — no logic lives here.
@Path("customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
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
