package airhacks.ebank.HEX_core.ports.out;

import airhacks.ebank.HEX_core.domain.Customer;

import java.util.Optional;

public interface CustomerPersistenceOutPort {

    Optional<Customer> findById(long id);

    boolean existsById(long id);

    Customer save(Customer customer);
}
