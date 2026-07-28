/// # Accounting
/// > Own the account lifecycle: initial creation with validation and balance lookup.
///
/// ## Boundary
/// - `create-account` — open an account with an IBAN and an initial balance
/// - `fetch-account` — look up an account by its IBAN
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
/// ## Entities
/// - Account
///
/// ## Out of scope
/// - Account closure, deletion, and updates outside transaction processing.
/// - Debit and deposit processing (owned by `transactions`).
package airhacks.ebank.accounting;
