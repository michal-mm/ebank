/// # Accounting
/// > Own the account lifecycle: initial creation with validation, balance lookup, and the assistant-facing account summary.
///
/// ## Boundary
/// - `create-account` — open an account with an IBAN and an initial balance
/// - `fetch-account` — look up an account by its IBAN
/// - `summarize-account` — summarize an account together with its owner, for an assistant
///   - MCP: `summarize_account` — prose summary of IBAN, balance, and owner
///
/// ## Requirements
/// ### R1: Create an account
/// - R1.1 — When an account with a positive initial balance below 1000 is submitted, the BC shall create the account and confirm the creation.
/// - R1.2 — If the initial balance is not positive, then the BC shall reject the creation as invalid.
/// - R1.3 — If the initial balance is 1000 or more, then the BC shall reject the creation as invalid. _(why: cap on initial deposits deters fraud)_
/// - R1.4 — If an account with the same IBAN already exists, then the BC shall reject the creation as a conflict.
///
/// ### R2: Fetch an account
/// - R2.1 — When a known IBAN is requested, the BC shall return the account with its IBAN and balance.
/// - R2.2 — If the IBAN is unknown, then the BC shall indicate absence without error.
///
/// ### R3: Summarize an account
/// - R3.1 — When a known IBAN is submitted, the BC shall return a summary carrying the account's IBAN, the account's balance, and the owning customer's id and name.
/// - R3.2 — If the account has no owner, then the BC shall return the summary and state that no customer owns the account. _(why: an account is created before it is associated, so being unowned is a normal state and not an error)_
/// - R3.3 — If the IBAN is unknown, then the BC shall indicate absence without error, phrased as a sentence. _(why: the caller is a language model, and a stated absence is read more reliably than an empty or null result)_
/// - R3.4 — While summarizing, the BC shall leave every account and ownership unchanged. _(why: the caller is non-deterministic, so the summary must expose no write surface)_
///
/// ## Entities
/// - Account
///
/// ## Decisions
/// - D1 — `summarize-account` is a boundary operation distinct from `fetch-account` rather than a second transport binding of it. _(why: it promises more than `fetch-account` — the owner — and an operation whose promise varies by transport stops being a contract; rejected: one op with an MCP sub-binding plus a `Where`-pattern statement scoped to that transport; one op widened so the HTTP lookup returns the owner too, which would change the existing `fetch-account` response)_
///
/// ## Out of scope
/// - Account closure, deletion, and updates outside transaction processing.
/// - Debit and deposit processing (owned by `transactions`).
/// - Assigning, changing, and removing ownership (owned by `customers`); `summarize-account` reads ownership and never alters it.
package airhacks.ebank.accounting;
