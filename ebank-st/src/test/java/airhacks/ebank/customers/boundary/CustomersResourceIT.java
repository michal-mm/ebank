package airhacks.ebank.customers.boundary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import airhacks.ebank.accounting.boundary.AccountDelegate;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.ws.rs.core.Response;

@QuarkusTest
class CustomersResourceIT {

    static final String UNKNOWN_CUSTOMER = "999999999";

    @Inject
    AccountDelegate accountDelegate;

    @Inject
    @RestClient
    CustomersResourceClient customers;

    @ParameterizedTest(name = "{0}")
    @MethodSource("registerCustomerCases")
    @DisplayName("R1: register a customer")
    void registerCustomer(String requirement, String name, int expectedStatus) {
        var response = this.register(name);
        assertThat(response.getStatus()).as(requirement).isEqualTo(expectedStatus);
        if (expectedStatus == 201) {
            var first = response.readEntity(JsonObject.class);
            var second = this.register(name).readEntity(JsonObject.class);
            assertThat(first.getJsonNumber("id").longValue())
                    .as(requirement + " — assigned ids are unique")
                    .isNotEqualTo(second.getJsonNumber("id").longValue());
        }
    }

    static Stream<Arguments> registerCustomerCases() {
        return Stream.of(
                arguments("R1.1", "duke", 201),
                arguments("R1.2", " ", 400));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("fetchCustomerCases")
    @DisplayName("R2: fetch a customer")
    void fetchCustomer(String requirement, boolean existing, int expectedStatus) {
        var name = "juggy";
        var id = existing ? this.registeredId(name) : UNKNOWN_CUSTOMER;
        var response = this.customers.fetchCustomer(id);
        assertThat(response.getStatus()).as(requirement).isEqualTo(expectedStatus);
        if (existing) {
            var customer = response.readEntity(JsonObject.class);
            assertThat(String.valueOf(customer.getJsonNumber("id").longValue())).as(requirement).isEqualTo(id);
            assertThat(customer.getString("name")).as(requirement).isEqualTo(name);
        }
    }

    static Stream<Arguments> fetchCustomerCases() {
        return Stream.of(
                arguments("R2.1", true, 200),
                arguments("R2.2", false, 204));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("ownAccountCases")
    @DisplayName("R3: own an account")
    void ownAccount(String requirement, boolean knownCustomer, boolean knownAccount,
            boolean accountAlreadyOwned, boolean ownsAnother, int expectedStatus) {
        var customerId = knownCustomer ? this.registeredId("duke") : UNKNOWN_CUSTOMER;
        var iban = knownAccount ? this.createAccount() : "unknown-" + UUID.randomUUID();
        if (accountAlreadyOwned)
            this.customers.ownAccount(this.registeredId("tux"), iban);
        if (ownsAnother)
            this.customers.ownAccount(customerId, this.createAccount());
        var response = this.customers.ownAccount(customerId, iban);
        assertThat(response.getStatus()).as(requirement).isEqualTo(expectedStatus);
    }

    static Stream<Arguments> ownAccountCases() {
        return Stream.of(
                arguments("R3.1", true, true, false, false, 201),
                arguments("R3.2", true, true, true, false, 409),
                arguments("R3.3", false, true, false, false, 400),
                arguments("R3.4", true, false, false, false, 400),
                arguments("R3.5", true, true, false, true, 201));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("listOwnedAccountsCases")
    @DisplayName("R4: list owned accounts")
    void listOwnedAccounts(String requirement, boolean knownCustomer, int ownedAccounts, int expectedStatus) {
        var customerId = knownCustomer ? this.registeredId("terracotta") : UNKNOWN_CUSTOMER;
        var ibans = IntStream.range(0, ownedAccounts)
                .mapToObj(i -> this.createAccount())
                .toList();
        ibans.forEach(iban -> this.customers.ownAccount(customerId, iban));
        var response = this.customers.listOwnedAccounts(customerId);
        assertThat(response.getStatus()).as(requirement).isEqualTo(expectedStatus);
        if (expectedStatus == 200) {
            var listed = response.readEntity(JsonArray.class)
                    .getValuesAs(JsonString.class)
                    .stream()
                    .map(JsonString::getString)
                    .toList();
            assertThat(listed).as(requirement).containsExactlyInAnyOrderElementsOf(ibans);
        }
    }

    static Stream<Arguments> listOwnedAccountsCases() {
        return Stream.of(
                arguments("R4.1", true, 2, 200),
                arguments("R4.2", false, 0, 204),
                arguments("R4.3", true, 0, 204));
    }

    Response register(String name) {
        var customerJSON = """
                {
                  "name": "%s"
                }
                """.formatted(name);
        return this.customers.registerCustomer(customerJSON);
    }

    String registeredId(String name) {
        var registered = this.register(name).readEntity(JsonObject.class);
        return String.valueOf(registered.getJsonNumber("id").longValue());
    }

    String createAccount() {
        var iban = UUID.randomUUID().toString();
        this.accountDelegate.initialCreation(iban, 42);
        return iban;
    }

}
