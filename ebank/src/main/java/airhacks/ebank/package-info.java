/// # eBank
/// > A banking API of cooperating business components for account creation, transaction processing, and reporting.
///
/// ## Vision
/// - A banking core so simple and observable that every behavior is explainable from the code alone.
///
/// ## Components
/// - `transactions` may call `accounting` (account lookup, shared result shaping); never the reverse.
/// - `reporting` reads `accounting`'s persisted account data directly, read-only; it never mutates.
/// - `customers` may call `accounting` to verify an account exists; never the reverse.
/// - `logging` and `health` are technical components: any BC may use `logging`; none may depend on `health`.
///
/// ## Ubiquitous language
/// - Account — a bank account identified by an IBAN, carrying a balance. Owned by `accounting`.
/// - IBAN — an account's unique identifier.
/// - Transaction — a deposit or debit of an amount applied to one account. Owned by `transactions`.
/// - Customer — a registered client of the bank, identified by a generated customer id. Owned by `customers`.
/// - Ownership — the association between a customer and an account they own; at most one owner per account. Owned by `customers`.
///
/// ## Stack
/// - microprofile-server · base package `airhacks.ebank`
package airhacks.ebank;
