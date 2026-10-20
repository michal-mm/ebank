package airhacks.ebank.HEX_core.usecase;

import airhacks.ebank.HEX_core.domain.Account;
import airhacks.ebank.HEX_core.domain.AccountSummary;
import airhacks.ebank.HEX_core.ports.out.AccountPersistenceOutPort;
import airhacks.ebank.HEX_core.ports.out.OwnershipPersistenceOutPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/// Joins an account with its owner into a single [AccountSummary]. The owner
/// is resolved through [OwnershipPersistenceOutPort] rather than by reading
/// the customers tables directly, so ownership stays that BC's secret
/// (see decision D1 in the system doc).
///
/// Both reads are lookups; nothing here writes, which is what keeps R3.4
/// true for every caller of the summary.
@ApplicationScoped
public class AccountSummaries {

    @Inject
    AccountPersistenceOutPort persistence;

    @Inject
    OwnershipPersistenceOutPort ownerships;

    public AccountSummary summary(String iban) {
        return persistence.findByIban(iban)
                .map(this::summarize)
                .orElseGet(() -> new AccountSummary.UnknownAccount(iban));
    }

    AccountSummary summarize(Account account) {
        return ownerships.findOwner(account.iban())
                .map(c -> (AccountSummary) new AccountSummary.Owned(account.iban(), account.balance(), c.id, c.name))
                .orElseGet(() -> new AccountSummary.Unowned(account.iban(), account.balance()));
    }
}
