package airhacks.ebank.customers.control;

public sealed interface OwnershipResult permits OwnershipResult.Owned,
        OwnershipResult.AlreadyOwned,
        OwnershipResult.UnknownCustomer,
        OwnershipResult.UnknownAccount {

    record Owned(long customerId, String iban) implements OwnershipResult {
    }

    record AlreadyOwned(String iban) implements OwnershipResult {
    }

    record UnknownCustomer(long customerId) implements OwnershipResult {
    }

    record UnknownAccount(String iban) implements OwnershipResult {
    }
}
