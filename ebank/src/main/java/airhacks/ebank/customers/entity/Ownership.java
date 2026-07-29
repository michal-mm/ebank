package airhacks.ebank.customers.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/// The association between a customer and an account: the IBAN is the identity,
/// so an account can carry at most one owner.
@Entity
public class Ownership {

    @Id
    public String iban;
    public Long customerId;

    protected Ownership() {
    }

    public Ownership(String iban, Long customerId) {
        this.iban = iban;
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "Ownership [iban=" + iban + ", customerId=" + customerId + "]";
    }

}
