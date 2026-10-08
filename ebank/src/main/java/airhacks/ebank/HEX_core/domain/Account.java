package airhacks.ebank.HEX_core.domain;

import java.math.BigDecimal;
import java.util.Objects;


/// A bank account with the IBAN as natural primary key. The balance changes
/// only through [#debit(BigDecimal)] and [#deposit(BigDecimal)];
/// [#tableName()] is the single source of truth for the native SQL in the
/// `reporting` BC.
public class Account {


    public String iban;

    public BigDecimal balance;

    protected Account() {
        this.balance = BigDecimal.ZERO;
    }

    public Account(String iban, BigDecimal balance) {
        this();
        Objects.requireNonNull(iban);
        Objects.requireNonNull(balance);
        this.iban = iban;
        this.balance = balance;
    }

    public Account debit(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
        return this;
    }

    public Account deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        return this;
    }

    public boolean isBalancePositive() {
        return this.balance.intValue() > 0;
    }

    public int balance() {
        return this.balance.intValue();
    }

    public String iban() {
        return this.iban;
    }

    public static String tableName() {
        return Account.class
                .getSimpleName()
                .toLowerCase();
    }

    @Override
    public String toString() {
        return "Account [iban=" + iban + ", balance=" + balance + "]";
    }

}
