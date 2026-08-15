package airhacks.ebank.accounting.control;

import java.util.Optional;

import airhacks.ebank.Control;
import airhacks.ebank.accounting.entity.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/// IBAN-based account lookup, shared across BCs: `transactions` resolves
/// accounts before applying deposits and debits, `customers` verifies
/// existence before recording ownership.
@Control
public class AccountFinder {
    @PersistenceContext
    EntityManager em;

    public Optional<Account> account(String iban) {
        var account = em.find(Account.class, iban);
        return Optional.ofNullable(account);
    }

    public boolean exists(Account account) {
        var iban = account.iban();
        return this.account(iban)
                .isPresent();
    }

}
