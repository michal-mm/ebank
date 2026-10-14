package airhacks.ebank.HEX_core.domain;

/// A registered bank customer identified by a generated id.
/// The name is the only mutable attribute; [#hasName()] is the single
/// validation predicate used at registration time.
public class Customer {

    public Long id;
    public String name;

    public Customer() {
    }

    public Customer(String name) {
        this.name = name;
    }

    public Customer(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean hasName() {
        return this.name != null && !this.name.isBlank();
    }

    @Override
    public String toString() {
        return "Customer [id=" + id + ", name=" + name + "]";
    }
}
