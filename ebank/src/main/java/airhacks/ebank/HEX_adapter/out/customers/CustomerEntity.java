package airhacks.ebank.HEX_adapter.out.customers;

import airhacks.ebank.HEX_core.domain.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
public class CustomerEntity {

    @Id
    @GeneratedValue
    @Schema(readOnly = true, examples = "1")
    public Long id;

    @Schema(required = true, examples = "duke")
    public String name;

    protected CustomerEntity() {
    }

    public static CustomerEntity from(Customer customer) {
        var entity = new CustomerEntity();
        entity.id = customer.id;
        entity.name = customer.name;
        return entity;
    }

    public Customer toDomain() {
        return new Customer(id, name);
    }
}
