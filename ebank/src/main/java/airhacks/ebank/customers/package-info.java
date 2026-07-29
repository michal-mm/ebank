/// # Customers
/// > Own the customer lifecycle and the ownership of accounts.
///
/// ## Boundary
/// - `register-customer` — register a customer with a name; the BC assigns a unique customer id
/// - `fetch-customer` — look up a customer by id
/// - `own-account` — associate an existing account with a customer
/// - `list-owned-accounts` — list the identifiers of the accounts a customer owns
///
/// ## Requirements
/// ### R1: Register a customer
/// - R1.1 — When a customer with a name is registered, the BC shall assign a unique customer id and confirm the registration.
/// - R1.2 — If the name is missing or blank, then the BC shall reject the registration as invalid.
///
/// ### R2: Fetch a customer
/// - R2.1 — When a known customer id is requested, the BC shall return the customer with its id and name.
/// - R2.2 — If the customer id is unknown, then the BC shall indicate absence without error.
///
/// ### R3: Own an account
/// - R3.1 — When an existing, unowned account is associated with a known customer, the BC shall record the customer as the account's owner and confirm the association.
/// - R3.2 — If the account is already owned, then the BC shall reject the association as a conflict. _(why: at most one owner per account; ownership transfer is a deliberate future capability)_
/// - R3.3 — If the customer is unknown, then the BC shall reject the association as invalid.
/// - R3.4 — If the account is unknown, then the BC shall reject the association as invalid.
/// - R3.5 — The BC shall allow a customer to own any number of accounts.
///
/// ### R4: List owned accounts
/// - R4.1 — When a known customer with owned accounts is requested, the BC shall return the identifiers of all accounts the customer owns.
/// - R4.2 — If the customer is unknown, then the BC shall indicate absence without error.
/// - R4.3 — While a known customer owns no accounts, the BC shall indicate absence without error.
///
/// ## Entities
/// - Customer, Ownership
///
/// ## Out of scope
/// - Customer update and deletion.
/// - Ownership transfer and co-ownership.
/// - Account creation, balances, and transactions (owned by `accounting` and `transactions`).
package airhacks.ebank.customers;
