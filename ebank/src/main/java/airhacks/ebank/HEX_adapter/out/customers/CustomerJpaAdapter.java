package airhacks.ebank.HEX_adapter.out.customers;

import airhacks.ebank.HEX_core.domain.Customer;
import airhacks.ebank.HEX_core.ports.out.CustomerPersistenceOutPort;
import airhacks.ebank.HEX_core.ports.out.OwnershipPersistenceOutPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CustomerJpaAdapter implements CustomerPersistenceOutPort, OwnershipPersistenceOutPort {

    @PersistenceContext
    EntityManager em;

    // --- CustomerPersistenceOutPort ---

    @Override
    public Optional<Customer> findById(long id) {
        return Optional.ofNullable(em.find(CustomerEntity.class, id))
                .map(CustomerEntity::toDomain);
    }

    @Override
    public boolean existsById(long id) {
        return em.find(CustomerEntity.class, id) != null;
    }

    @Override
    public Customer save(Customer customer) {
        var entity = CustomerEntity.from(customer);
        em.persist(entity);
        return entity.toDomain();
    }

    // --- OwnershipPersistenceOutPort ---

    @Override
    public boolean isOwned(String iban) {
        return em.find(OwnershipEntity.class, iban) != null;
    }

    @Override
    public void persistOwnership(String iban, long customerId) {
        em.persist(new OwnershipEntity(iban, customerId));
    }

    @Override
    public Optional<Customer> findOwner(String iban) {
        return Optional.ofNullable(em.find(OwnershipEntity.class, iban))
                .map(o -> o.customerId)
                .flatMap(this::findById);
    }

    @Override
    public List<String> ownedAccounts(long customerId) {
        return em.createQuery(
                        "SELECT o.iban FROM OwnershipEntity o WHERE o.customerId = :customerId",
                        String.class)
                .setParameter("customerId", customerId)
                .getResultList();
    }
}
