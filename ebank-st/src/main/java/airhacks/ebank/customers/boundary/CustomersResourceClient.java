package airhacks.ebank.customers.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = "ebank_uri")
public interface CustomersResourceClient {

    @POST
    Response registerCustomer(String customer);

    @GET
    @Path("{id}")
    Response fetchCustomer(@PathParam("id") String id);

    @POST
    @Path("{id}/accounts/{iban}")
    Response ownAccount(@PathParam("id") String id, @PathParam("iban") String iban);

    @GET
    @Path("{id}/accounts")
    Response listOwnedAccounts(@PathParam("id") String id);

}
