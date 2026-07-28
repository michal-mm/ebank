package airhacks.ebank.transactions.boundary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.UUID;
import java.util.stream.Stream;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import airhacks.ebank.accounting.boundary.AccountDelegate;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;

@QuarkusTest
class TransactionsResourceIT {

    @Inject
    AccountDelegate accountDelegate;

    @Inject
    @RestClient
    TransactionsResourceClient transactions;

    @ParameterizedTest(name = "{0}")
    @MethodSource("processTransactionCases")
    @DisplayName("R1: process a transaction")
    void processTransaction(String requirement, boolean existing, int initialBalance,
            String type, int amount, int expectedStatus, int expectedBalance) {
        var iban = UUID.randomUUID().toString();
        if (existing)
            this.accountDelegate.initialCreation(iban, initialBalance);
        var transactionJSON = """
                {
                  "type": "%s",
                  "amount": %d
                }
                """.formatted(type, amount);
        var response = this.transactions.processTransaction(iban, transactionJSON);
        assertThat(response.getStatus()).as(requirement).isEqualTo(expectedStatus);
        if (existing) {
            var account = response.readEntity(JsonObject.class);
            assertThat(account.getInt("balance")).as(requirement).isEqualTo(expectedBalance);
        }
    }

    static Stream<Arguments> processTransactionCases() {
        return Stream.of(
                arguments("R1.1", true, 100, "DEPOSIT", 50, 200, 150),
                arguments("R1.2", true, 100, "DEBIT", 30, 200, 70),
                arguments("R1.3", true, 100, "DEBIT", 500, 200, -400),
                arguments("R1.4", false, 0, "DEPOSIT", 10, 204, 0));
    }
}
