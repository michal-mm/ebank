package airhacks.ebank.accounting.control;

/// Closed set of account-summary outcomes. Sealing lets the boundary switch
/// exhaustively, so R3.1, R3.2 and R3.3 each get a rendering the compiler
/// insists on — a new outcome breaks the build instead of silently falling
/// through to a misleading sentence.
public sealed interface AccountSummary permits AccountSummary.Owned,
        AccountSummary.Unowned,
        AccountSummary.UnknownAccount {

    record Owned(String iban, int balance, long ownerId, String ownerName) implements AccountSummary {
    }

    record Unowned(String iban, int balance) implements AccountSummary {
    }

    record UnknownAccount(String iban) implements AccountSummary {
    }
}
