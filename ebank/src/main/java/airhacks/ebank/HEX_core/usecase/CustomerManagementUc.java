package airhacks.ebank.HEX_core.usecase;

import static airhacks.ebank.HEX_adapter.in.rest.customers.Requirement.Rn.R1_1;
import static airhacks.ebank.HEX_adapter.in.rest.customers.Requirement.Rn.R1_2;
import static airhacks.ebank.HEX_adapter.in.rest.customers.Requirement.Rn.R2_1;
import static airhacks.ebank.HEX_adapter.in.rest.customers.Requirement.Rn.R2_2;
import static airhacks.ebank.HEX_adapter.in.rest.customers.Requirement.Rn.R3_1;
import static airhacks.ebank.HEX_adapter.in.rest.customers.Requirement.Rn.R3_2;
import static airhacks.ebank.HEX_adapter.in.rest.customers.Requirement.Rn.R3_3;
import static airhacks.ebank.HEX_adapter.in.rest.customers.Requirement.Rn.R3_4;
import static airhacks.ebank.HEX_adapter.in.rest.customers.Requirement.Rn.R3_5;
import static airhacks.ebank.HEX_adapter.in.rest.customers.Requirement.Rn.R4_1;
import static airhacks.ebank.HEX_adapter.in.rest.customers.Requirement.Rn.R4_2;
import static airhacks.ebank.HEX_adapter.in.rest.customers.Requirement.Rn.R4_3;

import java.net.URI;

import airhacks.ebank.Boundary;
import airhacks.ebank.HEX_core.domain.Customer;
import airhacks.ebank.HEX_core.domain.OwnershipResult;
import airhacks.ebank.HEX_core.domain.OwnershipResult.AlreadyOwned;
import airhacks.ebank.HEX_core.domain.OwnershipResult.Owned;
import airhacks.ebank.HEX_core.domain.OwnershipResult.UnknownAccount;
import airhacks.ebank.HEX_core.domain.OwnershipResult.UnknownCustomer;
import airhacks.ebank.HEX_core.ports.in.CustomersInPort;
import airhacks.ebank.HEX_core.ports.out.AccountPersistenceOutPort;
import airhacks.ebank.HEX_core.ports.out.CustomerPersistenceOutPort;
import airhacks.ebank.HEX_core.ports.out.OwnershipPersistenceOutPort;
import airhacks.ebank.HEX_shared.logging.EBLog;
import airhacks.ebank.HEX_adapter.in.rest.customers.Requirement;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/// Orchestrates the customer lifecycle: registration, lookup, and account
/// ownership. Persistence is delegated to [CustomerPersistenceOutPort] and
/// [OwnershipPersistenceOutPort]; account existence checks go through
/// [AccountPersistenceOutPort]. No JPA or infrastructure concern lives here.
@Boundary
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerManagementUc implements CustomersInPort {

    @Inject
    CustomerPersistenceOutPort customers;

    @Inject
    OwnershipPersistenceOutPort ownerships;

    @Inject
    AccountPersistenceOutPort accounts;

    @Inject
    EBLog log;

    @Requirement({R1_1, R1_2})
    @Override
    public Response registerCustomer(Customer customer) {
        this.log.info("registerCustomer " + customer);
        if (customer == null || !customer.hasName())
            throw new BadRequestException("name is required");
        var registered = customers.save(customer);
        var uri = URI.create("/customers/" + registered.id);
        return Response
                .created(uri)
                .entity(registered)
                .build();
    }

    @Requirement({R2_1, R2_2})
    @Override
    public Response fetchCustomer(long id) {
        this.log.info("fetchCustomer " + id);
        return customers.findById(id)
                .map(c -> Response.ok(c).build())
                .orElseGet(() -> Response.noContent().build());
    }

    @Requirement({R3_1, R3_2, R3_3, R3_4, R3_5})
    @Override
    public Response ownAccount(long id, String iban) {
        this.log.info("ownAccount " + id + " " + iban);
        var result = resolveOwnership(id, iban);
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

    @Requirement({R4_1, R4_2, R4_3})
    @Override
    public Response listOwnedAccounts(long id) {
        this.log.info("listOwnedAccounts " + id);
        var ibans = ownerships.ownedAccounts(id);
        if (ibans.isEmpty())
            return Response.noContent().build();
        return Response.ok(ibans).build();
    }

    OwnershipResult resolveOwnership(long customerId, String iban) {
        if (!customers.existsById(customerId))
            return new OwnershipResult.UnknownCustomer(customerId);
        if (accounts.findByIban(iban).isEmpty())
            return new OwnershipResult.UnknownAccount(iban);
        if (ownerships.isOwned(iban))
            return new OwnershipResult.AlreadyOwned(iban);
        ownerships.persistOwnership(iban, customerId);
        return new OwnershipResult.Owned(customerId, iban);
    }
}
