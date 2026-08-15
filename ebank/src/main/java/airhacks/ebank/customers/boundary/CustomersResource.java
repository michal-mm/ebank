package airhacks.ebank.customers.boundary;

import static airhacks.ebank.customers.Requirement.Rn.R1_1;
import static airhacks.ebank.customers.Requirement.Rn.R1_2;
import static airhacks.ebank.customers.Requirement.Rn.R2_1;
import static airhacks.ebank.customers.Requirement.Rn.R2_2;
import static airhacks.ebank.customers.Requirement.Rn.R3_1;
import static airhacks.ebank.customers.Requirement.Rn.R3_2;
import static airhacks.ebank.customers.Requirement.Rn.R3_3;
import static airhacks.ebank.customers.Requirement.Rn.R3_4;
import static airhacks.ebank.customers.Requirement.Rn.R3_5;
import static airhacks.ebank.customers.Requirement.Rn.R4_1;
import static airhacks.ebank.customers.Requirement.Rn.R4_2;
import static airhacks.ebank.customers.Requirement.Rn.R4_3;

import java.net.URI;

import airhacks.ebank.Boundary;
import airhacks.ebank.customers.Requirement;
import airhacks.ebank.customers.control.AccountOwnership;
import airhacks.ebank.customers.control.CustomerFinder;
import airhacks.ebank.customers.control.CustomerRegistrar;
import airhacks.ebank.customers.control.OwnershipResult.AlreadyOwned;
import airhacks.ebank.customers.control.OwnershipResult.Owned;
import airhacks.ebank.customers.control.OwnershipResult.UnknownAccount;
import airhacks.ebank.customers.control.OwnershipResult.UnknownCustomer;
import airhacks.ebank.customers.entity.Customer;
import airhacks.ebank.logging.control.EBLog;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/// HTTP entry point for the customer lifecycle: registration, lookup, and
/// account ownership. Only the name check (R1.2) happens here; ownership
/// decisions are delegated to [AccountOwnership], whose exhaustive result
/// switch maps every rejection to a distinct HTTP status.
@Boundary
@Path("customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomersResource {

    @Inject
    CustomerRegistrar registrar;

    @Inject
    CustomerFinder finder;

    @Inject
    AccountOwnership ownership;

    @Inject
    EBLog log;

    @POST
    @Requirement({R1_1, R1_2})
    public Response registerCustomer(Customer customer) {
        this.log.info("registerCustomer " + customer);
        if (customer == null || !customer.hasName())
            throw new BadRequestException("name is required");
        var registered = this.registrar.register(customer);
        var uri = URI.create("/customers/" + registered.id);
        return Response
                .created(uri)
                .entity(registered)
                .build();
    }

    @GET
    @Path("{id}")
    @Requirement({R2_1, R2_2})
    public Response fetchCustomer(@PathParam("id") long id) {
        this.log.info("fetchCustomer " + id);
        return this.finder
                .customer(id)
                .map(customer -> Response.ok(customer).build())
                .orElseGet(() -> Response.noContent().build());
    }

    @POST
    @Path("{id}/accounts/{iban}")
    @Requirement({R3_1, R3_2, R3_3, R3_4, R3_5})
    public Response ownAccount(@PathParam("id") long id, @PathParam("iban") String iban) {
        this.log.info("ownAccount " + id + " " + iban);
        var result = this.ownership.own(id, iban);
        return switch (result) {
            case Owned _ -> Response
                    .created(URI.create("/customers/" + id + "/accounts/" + iban))
                    .build();
            case AlreadyOwned _ -> Response
                    .status(Response.Status.CONFLICT)
                    .build();
            case UnknownCustomer _, UnknownAccount _ -> Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        };
    }

    @GET
    @Path("{id}/accounts")
    @Requirement({R4_1, R4_2, R4_3})
    public Response listOwnedAccounts(@PathParam("id") long id) {
        this.log.info("listOwnedAccounts " + id);
        var ibans = this.ownership.ownedAccounts(id);
        if (ibans.isEmpty())
            return Response.noContent().build();
        return Response.ok(ibans).build();
    }

}
