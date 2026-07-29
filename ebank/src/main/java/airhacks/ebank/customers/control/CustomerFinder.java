package airhacks.ebank.customers.control;

import java.util.Optional;

import airhacks.ebank.Control;
import airhacks.ebank.customers.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Control
public class CustomerFinder {
    @PersistenceContext
    EntityManager em;

    public Optional<Customer> customer(long id) {
        var customer = em.find(Customer.class, id);
        return Optional.ofNullable(customer);
    }

    public boolean exists(long id) {
        return this.customer(id)
                .isPresent();
    }

}
