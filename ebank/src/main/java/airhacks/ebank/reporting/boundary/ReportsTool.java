package airhacks.ebank.reporting.boundary;

import static airhacks.ebank.reporting.Requirement.Rn.R1_1;
import static airhacks.ebank.reporting.Requirement.Rn.R1_2;

import airhacks.ebank.reporting.Requirement;
import airhacks.ebank.reporting.control.AccountQuery;
import io.quarkiverse.mcp.server.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;

/// MCP counterpart of [ReportsResource]: same capability, same control, a
/// different protocol. Both boundaries share [AccountQuery] directly — the CSV
/// rendering and the 204 in the JAX-RS resource are HTTP-only concerns and are
/// deliberately not reused here.
///
/// `reporting` is the BC exposed to AI assistants because it is read-only by
/// construction: [AccountQuery] reaches the accounts via JDBC and cannot mutate
/// them, so a non-deterministic caller has no write surface.
@ApplicationScoped
public class ReportsTool {

    @Inject
    AccountQuery accounts;

    /// @return a JSON array of IBANs, or a sentence when the bank holds no
    ///         accounts — the caller is a language model, so absence reads
    ///         better as prose than as an empty array
    @Tool(name = "list_account_ibans",
            title = "List account IBANs",
            description = """
                    List the IBANs of all accounts held by the bank. \
                    Use this to discover which accounts exist before inspecting a single one.""",
            annotations = @Tool.Annotations(
                    readOnlyHint = true,
                    idempotentHint = true,
                    openWorldHint = false))
    @Requirement({ R1_1, R1_2 })
    String listAccountIBANs() {
        var ibans = this.accounts.asIBANs();
        if (ibans.isEmpty())
            return "The bank holds no accounts.";
        return Json.createArrayBuilder(ibans)
                .build()
                .toString();
    }
}
