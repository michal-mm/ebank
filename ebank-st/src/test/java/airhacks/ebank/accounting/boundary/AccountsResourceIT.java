package airhacks.ebank.accounting.boundary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.UUID;
import java.util.stream.Stream;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;

@QuarkusTest
class AccountsResourceIT {

    @Inject
    AccountDelegate accountDelegate;

    @Inject
    @RestClient
    AccountsResourceClient accounts;

    @ParameterizedTest(name = "{0}")
    @MethodSource("createAccountCases")
    @DisplayName("R1: create an account")
    void createAccount(String requirement, boolean alreadyExisting, int initialBalance, int expectedStatus) {
        var iban = randomIBAN();
        if (alreadyExisting)
            this.accountDelegate.initialCreation(iban, initialBalance);
        var response = this.accountDelegate.initialCreation(iban, initialBalance);
        assertThat(response.getStatus()).as(requirement).isEqualTo(expectedStatus);
    }

    static Stream<Arguments> createAccountCases() {
        return Stream.of(
                arguments("R1.1", false, 900, 201),
                arguments("R1.2", false, -1, 400),
                arguments("R1.3", false, 1000, 400),
                arguments("R1.4", true, 2, 409));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("fetchAccountCases")
    @DisplayName("R2: fetch an account")
    void fetchAccount(String requirement, boolean existing, int expectedStatus) {
        var iban = randomIBAN();
        var balance = 42;
        if (existing)
            this.accountDelegate.initialCreation(iban, balance);
        var response = this.accounts.account(iban);
        assertThat(response.getStatus()).as(requirement).isEqualTo(expectedStatus);
        if (existing) {
            var account = response.readEntity(JsonObject.class);
            assertThat(account.getString("iban")).as(requirement).isEqualTo(iban);
            assertThat(account.getInt("balance")).as(requirement).isEqualTo(balance);
        }
    }

    static Stream<Arguments> fetchAccountCases() {
        return Stream.of(
                arguments("R2.1", true, 200),
                arguments("R2.2", false, 204));
    }

    static String randomIBAN() {
        return UUID.randomUUID().toString();
    }

    @Test
    @DisplayName("tests account transactions after initial creation")
    void creationDepositAndDebit() {
        var balance = 900;
        var randomIBAN = this.accountDelegate.randomIBAN();
        var account = this.accountDelegate.initialCreationAndFetch(randomIBAN, balance);
        assertThat(account.getString("iban")).isEqualTo(randomIBAN);
        assertThat(account.getInt("balance")).isEqualTo(balance);
        var balanceAfterDebit = this.accountDelegate.debit(900);
        assertThat(balanceAfterDebit).isEqualTo(balance - 900);

        var balanceAfterDeposit = this.accountDelegate.deposit(100);
        assertThat(balanceAfterDeposit).isEqualTo(balanceAfterDebit + 100);
    }
}
