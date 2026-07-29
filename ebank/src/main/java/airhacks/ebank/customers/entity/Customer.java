package airhacks.ebank.customers.entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
public class Customer {

    @Id
    @GeneratedValue
    @Schema(readOnly = true, examples = "1")
    public Long id;
    @Schema(required = true, examples = "duke")
    public String name;

    public Customer() {
    }

    public Customer(String name) {
        this.name = name;
    }

    @JsonbTransient
    @Schema(hidden = true)
    public boolean hasName() {
        return this.name != null && !this.name.isBlank();
    }

    @Override
    public String toString() {
        return "Customer [id=" + id + ", name=" + name + "]";
    }

}
