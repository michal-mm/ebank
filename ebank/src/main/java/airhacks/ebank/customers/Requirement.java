package airhacks.ebank.customers;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// Generated from the capability spec in [airhacks.ebank.customers] — do not edit.
/// Marks the boundary method or test that realizes the given requirement statements.
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Requirement {

    /// One constant per statement id in the spec's `## Requirements`.
    enum Rn {
        /// When a customer with a name is registered, the BC shall assign a unique customer id and confirm the registration.
        R1_1("R1.1", "When a customer with a name is registered, the BC shall assign a unique customer id and confirm the registration."),
        /// If the name is missing or blank, then the BC shall reject the registration as invalid.
        R1_2("R1.2", "If the name is missing or blank, then the BC shall reject the registration as invalid."),
        /// When a known customer id is requested, the BC shall return the customer with its id and name.
        R2_1("R2.1", "When a known customer id is requested, the BC shall return the customer with its id and name."),
        /// If the customer id is unknown, then the BC shall indicate absence without error.
        R2_2("R2.2", "If the customer id is unknown, then the BC shall indicate absence without error."),
        /// When an existing, unowned account is associated with a known customer, the BC shall record the customer as the account's owner and confirm the association.
        R3_1("R3.1", "When an existing, unowned account is associated with a known customer, the BC shall record the customer as the account's owner and confirm the association."),
        /// If the account is already owned, then the BC shall reject the association as a conflict.
        R3_2("R3.2", "If the account is already owned, then the BC shall reject the association as a conflict."),
        /// If the customer is unknown, then the BC shall reject the association as invalid.
        R3_3("R3.3", "If the customer is unknown, then the BC shall reject the association as invalid."),
        /// If the account is unknown, then the BC shall reject the association as invalid.
        R3_4("R3.4", "If the account is unknown, then the BC shall reject the association as invalid."),
        /// The BC shall allow a customer to own any number of accounts.
        R3_5("R3.5", "The BC shall allow a customer to own any number of accounts."),
        /// When a known customer with owned accounts is requested, the BC shall return the identifiers of all accounts the customer owns.
        R4_1("R4.1", "When a known customer with owned accounts is requested, the BC shall return the identifiers of all accounts the customer owns."),
        /// If the customer is unknown, then the BC shall indicate absence without error.
        R4_2("R4.2", "If the customer is unknown, then the BC shall indicate absence without error."),
        /// While a known customer owns no accounts, the BC shall indicate absence without error.
        R4_3("R4.3", "While a known customer owns no accounts, the BC shall indicate absence without error.");

        private final String id;
        private final String statement;

        Rn(String id, String statement) {
            this.id = id;
            this.statement = statement;
        }

        public String statement() {
            return statement;
        }

        @Override
        public String toString() {
            return id;
        }
    }

    Rn[] value();
}
