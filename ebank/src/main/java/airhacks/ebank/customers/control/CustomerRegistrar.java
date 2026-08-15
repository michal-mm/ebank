package airhacks.ebank.customers.control;

import airhacks.ebank.Control;
import airhacks.ebank.customers.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/// Persists new customers. The returned instance carries the id generated on
/// persist (R1.1), which the boundary echoes back in the `Location` header.
@Control
public class CustomerRegistrar {
    @PersistenceContext
    EntityManager em;

    public Customer register(Customer customer) {
        this.em.persist(customer);
        return customer;
    }

}
