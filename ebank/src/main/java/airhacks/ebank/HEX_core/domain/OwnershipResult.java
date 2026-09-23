package airhacks.ebank.HEX_core.domain;

import airhacks.ebank.customers.control.AccountOwnership;

/// Closed set of ownership outcomes. Sealing lets the boundary switch
/// exhaustively, so a new outcome breaks compilation instead of silently
/// falling through to a wrong HTTP status.
///
/// Returned by [AccountOwnership#own(long,String)] and consumed by
/// `CustomersResource#ownAccount(long,String)`. The variants carry no status
/// codes or messages: the control decides *what* happened, the boundary decides
/// how that reads over HTTP. Rejections are results rather than exceptions
/// because none of them are exceptional — each is an expected answer to a
/// request that a client can legitimately send.
public sealed interface OwnershipResult permits OwnershipResult.Owned,
        OwnershipResult.AlreadyOwned,
        OwnershipResult.UnknownCustomer,
        OwnershipResult.UnknownAccount {

    /// The association was recorded (R3.1). The only variant with a persistent
    /// side effect; it echoes both identifiers so the boundary can build the
    /// created resource's location without re-reading them.
    record Owned(long customerId, String iban) implements OwnershipResult {
    }

    /// The account already has an owner (R3.2) — a conflict, not a bad request:
    /// the input was well-formed and both parties exist, the account is simply
    /// taken. Carries no customer id, because the rejection is about the
    /// account's existing owner, not the requesting one.
    record AlreadyOwned(String iban) implements OwnershipResult {
    }

    /// No customer with this id (R3.3). Kept distinct from [UnknownAccount]
    /// even though both currently answer with the same status, so the reason
    /// stays available for logging and for a later, more precise response.
    record UnknownCustomer(long customerId) implements OwnershipResult {
    }

    /// No account with this IBAN (R3.4). The counterpart to [UnknownCustomer];
    /// the account's existence is checked against `accounting` rather than the
    /// local [airhacks.ebank.customers.entity.Ownership] table, which only
    /// knows accounts that are already owned.
    record UnknownAccount(String iban) implements OwnershipResult {
    }
}
