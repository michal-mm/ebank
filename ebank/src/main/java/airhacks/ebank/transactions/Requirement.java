package airhacks.ebank.transactions;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// Generated from the capability spec in [airhacks.ebank.transactions] — do not edit.
/// Marks the boundary method or test that realizes the given requirement statements.
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Requirement {

    /// One constant per statement id in the spec's `## Requirements`.
    enum Rn {
        /// When a deposit is applied to an existing account, the BC shall increase the balance by the amount and return the updated account.
        R1_1("R1.1", "When a deposit is applied to an existing account, the BC shall increase the balance by the amount and return the updated account."),
        /// When a debit is applied to an existing account, the BC shall decrease the balance by the amount and return the updated account.
        R1_2("R1.2", "When a debit is applied to an existing account, the BC shall decrease the balance by the amount and return the updated account."),
        /// The BC shall apply debits unconditionally; the balance may become negative.
        R1_3("R1.3", "The BC shall apply debits unconditionally; the balance may become negative."),
        /// If the account is unknown, then the BC shall indicate absence without error.
        R1_4("R1.4", "If the account is unknown, then the BC shall indicate absence without error.");

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
