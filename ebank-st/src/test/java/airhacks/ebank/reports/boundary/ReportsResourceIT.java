package airhacks.ebank.reports.boundary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.UUID;
import java.util.stream.Stream;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import airhacks.ebank.accounting.boundary.AccountDelegate;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

/**
 * Runs first in the suite (see junit-platform.properties): the R1.2 row
 * requires the freshly started system with no accounts, before any other
 * test creates one.
 */
@QuarkusTest
@Order(1)
public class ReportsResourceIT {

    @Inject
    AccountDelegate accountDelegate;

    @Inject
    @RestClient
    ReportsResourceClient rut;

    @ParameterizedTest(name = "{0}")
    @MethodSource("reportCases")
    @DisplayName("R1: report account identifiers")
    void reportAccountIBANs(String requirement, int accountsToCreate, int expectedStatus) {
        var ibans = Stream.generate(() -> UUID.randomUUID().toString())
                .limit(accountsToCreate)
                .toList();
        ibans.forEach(iban -> this.accountDelegate.initialCreation(iban, 42));
        var response = this.rut.accounts();
        assertThat(response.getStatus()).as(requirement).isEqualTo(expectedStatus);
        if (!ibans.isEmpty()) {
            var csv = response.readEntity(String.class);
            assertThat(csv).as(requirement)
                    .contains(ibans.toArray(String[]::new))
                    .contains(",");
        }
    }

    static Stream<Arguments> reportCases() {
        return Stream.of(
                arguments("R1.2", 0, 204),
                arguments("R1.1", 2, 200));
    }
}
