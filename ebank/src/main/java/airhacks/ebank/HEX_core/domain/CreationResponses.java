package airhacks.ebank.HEX_core.domain;

import java.net.URI;

import airhacks.ebank.HEX_core.domain.AccountCreationResult.AlreadyExists;
import airhacks.ebank.HEX_core.domain.AccountCreationResult.Created;
import airhacks.ebank.HEX_core.domain.AccountCreationResult.Invalid;
import airhacks.ebank.HEX_shared.Responses;
import jakarta.ws.rs.core.Response;

/// Translates the sealed [AccountCreationResult]
/// into HTTP status codes. Accounting-specific and therefore owned by this
/// boundary; the status-code plumbing itself comes from [Responses].
public interface CreationResponses {

    static Response created(Created created) {
        var iban = created.account().iban();
        return Responses.created(URI.create("/" + iban));
    }

    static Response alreadyExists(AlreadyExists exists) {
        return Responses.conflict(exists);
    }

    static Response invalid(Invalid invalid) {
        return Responses.badRequest(invalid);
    }
}
