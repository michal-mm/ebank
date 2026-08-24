package airhacks.ebank.accounting.boundary;

import static airhacks.ebank.accounting.Requirement.Rn.R3_1;
import static airhacks.ebank.accounting.Requirement.Rn.R3_2;
import static airhacks.ebank.accounting.Requirement.Rn.R3_3;
import static airhacks.ebank.accounting.Requirement.Rn.R3_4;

import airhacks.ebank.accounting.Requirement;
import airhacks.ebank.accounting.control.AccountSummaries;
import airhacks.ebank.accounting.control.AccountSummary.Owned;
import airhacks.ebank.accounting.control.AccountSummary.Unowned;
import airhacks.ebank.accounting.control.AccountSummary.UnknownAccount;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/// Assistant-facing counterpart of [AccountsResource]: the same BC, a
/// different protocol, and deliberately only the reading half of it —
/// `create-account` stays behind the HTTP boundary, where a human is in the
/// loop.
///
/// Unlike the resource, this class carries no `@Boundary` stereotype and
/// therefore starts no transaction: [AccountSummaries] only looks up, so the
/// tool has no write surface for a non-deterministic caller to reach (R3.4).
///
/// Every outcome is rendered as a sentence rather than as JSON — the caller
/// is a language model, which reads a stated absence more reliably than an
/// empty result.
@ApplicationScoped
public class AccountsTool {

    @Inject
    AccountSummaries summaries;

    /// @return one sentence naming the balance and the owner, or stating that
    ///         the account is unowned or that no such account exists
    @Tool(name = "summarize_account",
            title = "Summarize an account",
            description = """
                    Summarize one bank account: its IBAN, its current balance, and the customer who owns it. \
                    Use this to inspect a single account after discovering IBANs with list_account_ibans. \
                    Reports an unowned account and an unknown IBAN as plain statements rather than as errors. \
                    Does not return the account's transaction history, and never creates or modifies anything.""",
            annotations = @Tool.Annotations(
                    readOnlyHint = true,
                    idempotentHint = true,
                    openWorldHint = false))
    @Requirement({ R3_1, R3_2, R3_3, R3_4 })
    String summarizeAccount(
            @ToolArg(description = """
                    The IBAN identifying the account, exactly as returned by list_account_ibans. \
                    Matched in full; partial IBANs and account holder names do not resolve.""") String iban) {
        return switch (this.summaries.summary(iban)) {
            case Owned owned -> "Account %s holds a balance of %d and is owned by customer %d, %s."
                    .formatted(owned.iban(), owned.balance(), owned.ownerId(), owned.ownerName());
            case Unowned unowned -> "Account %s holds a balance of %d and is owned by no customer."
                    .formatted(unowned.iban(), unowned.balance());
            case UnknownAccount unknown -> "No account exists with IBAN %s."
                    .formatted(unknown.iban());
        };
    }
}
