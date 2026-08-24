package airhacks.ebank.customers.control;

import java.util.List;
import java.util.Optional;

import airhacks.ebank.Control;
import airhacks.ebank.accounting.control.AccountFinder;
import airhacks.ebank.customers.entity.Customer;
import airhacks.ebank.customers.entity.Ownership;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/// Records which customer owns which account. Customer and account existence
/// are verified before an [Ownership] is persisted; its IBAN primary key
/// enforces at most one owner per account (R3.2), while a customer may own any
/// number of accounts (R3.5).
@Control
public class AccountOwnership {
    @PersistenceContext
    EntityManager em;

    @Inject
    CustomerFinder customers;

    @Inject
    AccountFinder accounts;

    public OwnershipResult own(long customerId, String iban) {
        if (!this.customers.exists(customerId))
            return new OwnershipResult.UnknownCustomer(customerId);
        if (this.accounts.account(iban).isEmpty())
            return new OwnershipResult.UnknownAccount(iban);
        if (this.em.find(Ownership.class, iban) != null)
            return new OwnershipResult.AlreadyOwned(iban);
        this.em.persist(new Ownership(iban, customerId));
        return new OwnershipResult.Owned(customerId, iban);
    }

    /// Resolves an account's owner for `accounting`'s account summary — the
    /// inverse of [#ownedAccounts(long)], and the only read `accounting` needs
    /// to avoid touching [Ownership] itself.
    ///
    /// @return the owning customer, or empty when the account is unowned or
    ///         unknown — both are normal states, not errors
    public Optional<Customer> owner(String iban) {
        return Optional.ofNullable(this.em.find(Ownership.class, iban))
                .map(ownership -> ownership.customerId)
                .flatMap(this.customers::customer);
    }

    public List<String> ownedAccounts(long customerId) {
        return this.em
                .createQuery("SELECT o.iban FROM Ownership o WHERE o.customerId = :customerId", String.class)
                .setParameter("customerId", customerId)
                .getResultList();
    }

}
