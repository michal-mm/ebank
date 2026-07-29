package airhacks.ebank.accounting.control;

import airhacks.ebank.Control;
import airhacks.ebank.accounting.entity.Account;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Control
public class AccountCreator {
    @PersistenceContext
    EntityManager em;

    @Inject
    AccountFinder finder;

    public AccountCreationResult initialCreation(Account account) {
        if (!this.isValidForCreation(account))
            return new AccountCreationResult.Invalid(account);
        if (this.finder.exists(account))
            return new AccountCreationResult.AlreadyExists(account);
        this.em.persist(account);
        return new AccountCreationResult.Created(account);
    }

    boolean isValidForCreation(Account account) {
        return account.isBalancePositive()
                && (account.balance() < 1000);
    }
}
