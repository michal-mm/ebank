/// # eBank
/// > A banking API of cooperating business components for account creation, transaction processing, and reporting.
///
/// ## Vision
/// - A banking core so simple and observable that every behavior is explainable from the code alone.
///
/// ## Components
/// - `transactions` may call `accounting` (account lookup, shared result shaping); never the reverse.
/// - `reporting` reads `accounting`'s persisted account data directly, read-only; it never mutates.
/// - `customers` may call `accounting` to verify an account exists.
/// - `accounting` may call `customers` to resolve an account's owner, read-only. `accounting` and `customers` are therefore mutually dependent — a deliberate exception, see D1.
/// - `logging` and `health` are technical components: any BC may use `logging`; none may depend on `health`.
///
/// ## Ubiquitous language
/// - Account — a bank account identified by an IBAN, carrying a balance. Owned by `accounting`.
/// - IBAN — an account's unique identifier.
/// - Transaction — a deposit or debit of an amount applied to one account. Owned by `transactions`.
/// - Customer — a registered client of the bank, identified by a generated customer id. Owned by `customers`.
/// - Ownership — the association between a customer and an account they own; at most one owner per account. Owned by `customers`.
///
/// ## Decisions
/// - D1 — The assistant-facing account summary lives in `accounting` and calls `customers` read-only to resolve the owner; `## Components` admits the resulting mutual dependency. _(why: the summary is an account capability and belongs with the BC that owns Account; rejected: hosting it in `reporting`, read-only by construction but needing the same new edge and a charter widened beyond the IBAN list; hosting it in `customers`, preserving the one-way edge but moving an account capability out of `accounting`; reading `customers`' ownership table directly over JDBC, avoiding the cycle but coupling `accounting` to another BC's persisted schema)_
///
/// ## Stack
/// - microprofile-server · base package `airhacks.ebank`
package airhacks.ebank;
