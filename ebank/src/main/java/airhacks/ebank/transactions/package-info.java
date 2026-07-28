/// # Transactions
/// > Apply deposit and debit transactions to existing accounts.
///
/// ## Boundary
/// - `process-transaction` — apply a deposit or debit of an amount to an account
///
/// ## Requirements
/// ### R1: Process a transaction
/// - R1.1 — When a deposit is applied to an existing account, the BC shall increase the balance by the amount and return the updated account.
/// - R1.2 — When a debit is applied to an existing account, the BC shall decrease the balance by the amount and return the updated account.
/// - R1.3 — The BC shall apply debits unconditionally; the balance may become negative. _(why: overdraft handling is deliberately outside this demo's scope)_
/// - R1.4 — If the account is unknown, then the BC shall indicate absence without error.
///
/// ## Entities
/// - Transaction
///
/// ## Out of scope
/// - Amount validation (negative or zero amounts are applied as given).
/// - Transaction history, auditing, and transfers between accounts.
package airhacks.ebank.transactions;
