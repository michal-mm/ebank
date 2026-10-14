package airhacks.ebank.HEX_core.ports.out;

import airhacks.ebank.HEX_core.domain.Customer;

import java.util.List;
import java.util.Optional;

public interface OwnershipPersistenceOutPort {

    boolean isOwned(String iban);

    void persistOwnership(String iban, long customerId);

    Optional<Customer> findOwner(String iban);

    List<String> ownedAccounts(long customerId);
}
