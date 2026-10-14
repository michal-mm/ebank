package airhacks.ebank.HEX_adapter.out.customers;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/// JPA entity for the ownership association. The IBAN primary key enforces
/// at most one owner per account at the persistence level.
@Entity
public class OwnershipEntity {

    @Id
    public String iban;
    public Long customerId;

    protected OwnershipEntity() {
    }

    public OwnershipEntity(String iban, Long customerId) {
        this.iban = iban;
        this.customerId = customerId;
    }
}
