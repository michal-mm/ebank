package airhacks.ebank.reports.boundary;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import airhacks.ebank.accounting.boundary.AccountDelegate;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest 
public class ReportsResourceIT {
    @Inject
    AccountDelegate accountDelegate;

    @Inject
    @RestClient
    ReportsResourceClient rut;

    @Test
    void fetchIBANs(){
        //initial fetch, the list can be empty
        var response = this.rut.accounts();
        assertThat(response.getStatus()).isBetween(200, 204);
        
        //new account created
        var createdAccountIBAN = UUID.randomUUID().toString();
        accountDelegate.initialCreationAndFetch(createdAccountIBAN, 42);

        response = this.rut.accounts();
        assertThat(response.getStatus()).isEqualTo(200);
        var ibanList = response.readEntity(String.class);

        //the list should include the iban of the created account
        assertThat(ibanList).contains(createdAccountIBAN);
    }

}
