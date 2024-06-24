package StationDataCollector.StationDataCollector.entity;

import jakarta.persistence.*;

@Entity
public class Charge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float kwh;

    @Column(name = "customer_id")
    private int customerId;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getKwh() {
        return kwh;
    }

    public void setKwh(float kwh) {
        this.kwh = kwh;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "Charge{" +
                "id=" + id +
                ", kwh=" + kwh +
                ", customerId=" + customerId +
                '}';
    }
}
