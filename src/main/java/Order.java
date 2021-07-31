import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Order {
    private UUID id;
    private String name;
    private LocalDate date;
    private Map<Product, Integer> productMap;

    public Order(UUID id, LocalDate date) {
        this.id = id;
        this.date = date;
        this.productMap = new HashMap<>();
    }

    public void putProduct(Product product, int count) {
        productMap.put(product, count);
    }

    public Map<Product, Integer> getProductMap() {
        return productMap;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", datetime="+ date + "}";
    }
}
