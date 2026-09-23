package airhacks.ebank.HEX_adapter.out;

import airhacks.ebank.HEX_core.ports.out.PersistenceOutPort;
import airhacks.ebank.accounting.entity.Account;

public class AccountJpaAdapter implements PersistenceOutPort {
    @Override
    public void save(Account account) {
        // TODO
    }

    @Override
    public void exists(String iban) {
        // TODO
    }
}
