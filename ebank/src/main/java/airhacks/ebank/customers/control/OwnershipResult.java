package airhacks.ebank.customers.control;

/// Closed set of ownership outcomes. Sealing lets the boundary switch
/// exhaustively, so a new outcome breaks compilation instead of silently
/// falling through to a wrong HTTP status.
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
