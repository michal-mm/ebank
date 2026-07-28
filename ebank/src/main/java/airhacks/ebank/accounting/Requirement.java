package airhacks.ebank.accounting;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// Generated from the capability spec in [airhacks.ebank.accounting] — do not edit.
/// Marks the boundary method or test that realizes the given requirement statements.
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Requirement {

    /// One constant per statement id in the spec's `## Requirements`.
    enum Rn {
        /// When an account with a positive initial balance below 1000 is submitted, the BC shall create the account and confirm the creation.
        R1_1("R1.1", "When an account with a positive initial balance below 1000 is submitted, the BC shall create the account and confirm the creation."),
        /// If the initial balance is not positive, then the BC shall reject the creation as invalid.
        R1_2("R1.2", "If the initial balance is not positive, then the BC shall reject the creation as invalid."),
        /// If the initial balance is 1000 or more, then the BC shall reject the creation as invalid.
        R1_3("R1.3", "If the initial balance is 1000 or more, then the BC shall reject the creation as invalid."),
        /// If an account with the same IBAN already exists, then the BC shall reject the creation as a conflict.
        R1_4("R1.4", "If an account with the same IBAN already exists, then the BC shall reject the creation as a conflict."),
        /// When a known IBAN is requested, the BC shall return the account with its IBAN and balance.
        R2_1("R2.1", "When a known IBAN is requested, the BC shall return the account with its IBAN and balance."),
        /// If the IBAN is unknown, then the BC shall indicate absence without error.
        R2_2("R2.2", "If the IBAN is unknown, then the BC shall indicate absence without error.");

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
