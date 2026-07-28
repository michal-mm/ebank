/// # Reporting
/// > Read-only extraction of account data for administrative oversight.
///
/// ## Boundary
/// - `report-account-ibans` — list the identifiers of all accounts
///
/// ## Requirements
/// ### R1: Report account identifiers
/// - R1.1 — When accounts exist, the BC shall return the IBANs of all accounts as a comma-separated list.
/// - R1.2 — If no accounts exist, then the BC shall indicate absence without error.
///
/// ## Out of scope
/// - Aggregations, balances, and any report beyond the IBAN list.
/// - Mutating account data (owned by `accounting` and `transactions`).
package airhacks.ebank.reporting;
