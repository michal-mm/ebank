package airhacks.ebank.customers.control;

import airhacks.ebank.Control;
import airhacks.ebank.customers.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Control
public class CustomerRegistrar {
    @PersistenceContext
    EntityManager em;

    public Customer register(Customer customer) {
        this.em.persist(customer);
        return customer;
    }

}
