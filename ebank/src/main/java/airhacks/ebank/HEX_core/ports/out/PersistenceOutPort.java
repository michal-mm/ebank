package airhacks.ebank.HEX_core.ports.out;

import airhacks.ebank.accounting.entity.Account;

public interface PersistenceOutPort {

    void save(Account account);

    void exists(String iban);
    
    // TODO (mm) - define persistence methods...
}
