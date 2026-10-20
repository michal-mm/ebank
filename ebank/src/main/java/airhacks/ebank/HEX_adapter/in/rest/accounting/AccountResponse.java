package airhacks.ebank.HEX_adapter.in.rest.accounting;

import java.math.BigDecimal;

import airhacks.ebank.HEX_core.domain.Account;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/// Wire representation of an account returned by the HTTP adapter.
/// Keeps serialization and OpenAPI concerns out of the domain [Account].
public record AccountResponse(
        @Schema(required = true, examples = "DE89370400440532013000") String iban,
        @Schema(required = true, examples = "42") BigDecimal balance) {

    public static AccountResponse from(Account account) {
        return new AccountResponse(account.iban(), account.balance);
    }
}
