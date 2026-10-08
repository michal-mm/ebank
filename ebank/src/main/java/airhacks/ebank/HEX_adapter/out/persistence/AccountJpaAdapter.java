package airhacks.ebank.HEX_adapter.out.persistence;

import airhacks.ebank.HEX_adapter.out.accounting.AccountEntity;
import airhacks.ebank.HEX_core.ports.out.AccountPersistenceOutPort;
import airhacks.ebank.HEX_core.domain.Account;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

@ApplicationScoped
public class AccountJpaAdapter implements AccountPersistenceOutPort {

    @PersistenceContext
    EntityManager em;

    @Override
    public Optional<Account> findByIban(String iban) {
        return Optional.ofNullable(em.find(AccountEntity.class, iban))
                .map(AccountEntity::toDomain);
    }

    @Override
    public boolean existsByIban(String iban) {
        return em.find(AccountEntity.class, iban) != null;
    }

    @Override
    public Account save(Account account) {
        var entity = AccountEntity.from(account);
        em.persist(entity);
        return entity.toDomain();
    }

    @Override
    public Account merge(Account account) {
        var entity = AccountEntity.from(account);
        return em.merge(entity).toDomain();
    }
}

