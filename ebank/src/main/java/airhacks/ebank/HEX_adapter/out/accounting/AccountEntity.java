package airhacks.ebank.HEX_adapter.out.accounting;

import airhacks.ebank.HEX_core.domain.Account;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Entity
public class AccountEntity {

    @Id
    @Schema(required = true, examples = "2")
    public String iban;

    @Schema(required = true, examples = "42")
    public BigDecimal balance;

    protected AccountEntity() {}

    public static  AccountEntity from(Account account) {
        AccountEntity entity = new AccountEntity();
        entity.iban = account.iban;
        entity.balance = account.balance;

        return entity;
    }

    public Account toDomain() {
        // should actually be a DTO that is translated to domain using specific mapper...
        return new Account(iban, balance);
    }
}
