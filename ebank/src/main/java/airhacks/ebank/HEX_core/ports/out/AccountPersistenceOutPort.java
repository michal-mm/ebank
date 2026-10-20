package airhacks.ebank.HEX_core.ports.out;

import airhacks.ebank.HEX_core.domain.Account;

import java.util.Optional;

public interface AccountPersistenceOutPort {

    Optional<Account> findByIban(String iban);

    boolean existsByIban(String iban);

    Account save(Account account);

    Account merge(Account account);
}
