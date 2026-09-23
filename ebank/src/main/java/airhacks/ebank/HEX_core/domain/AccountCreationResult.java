package airhacks.ebank.HEX_core.domain;

import airhacks.ebank.accounting.entity.Account;

/// Closed set of account-creation outcomes. Sealing lets the boundary switch
/// exhaustively, so a new outcome breaks compilation instead of silently
/// falling through to a wrong HTTP status.
public sealed interface AccountCreationResult permits AccountCreationResult.AlreadyExists,
        AccountCreationResult.Invalid,
        AccountCreationResult.Created {
            
    record AlreadyExists(Account account) implements AccountCreationResult {
    }

    record Invalid(Account account) implements AccountCreationResult {
    }

    record Created(Account account) implements AccountCreationResult {
    }
}