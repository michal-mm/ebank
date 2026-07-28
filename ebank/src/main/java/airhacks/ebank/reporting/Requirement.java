package airhacks.ebank.reporting;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// Generated from the capability spec in [airhacks.ebank.reporting] — do not edit.
/// Marks the boundary method or test that realizes the given requirement statements.
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Requirement {

    /// One constant per statement id in the spec's `## Requirements`.
    enum Rn {
        /// When accounts exist, the BC shall return the IBANs of all accounts as a comma-separated list.
        R1_1("R1.1", "When accounts exist, the BC shall return the IBANs of all accounts as a comma-separated list."),
        /// If no accounts exist, then the BC shall indicate absence without error.
        R1_2("R1.2", "If no accounts exist, then the BC shall indicate absence without error.");

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
