package DataCollectionReceiver.DataCollectionReceiver.entity;

import org.json.JSONObject;

public class Charge {
    private Long id;
    private Double kwh;
    private Long customerId;

    // Constructors
    public Charge() {}

    public Charge(Long id, Double kwh, Long customerId) {
        this.id = id;
        this.kwh = kwh;
        this.customerId = customerId;
    }

    public Charge(JSONObject jsonObject) {
        this.id = jsonObject.getLong("id");
        this.kwh = jsonObject.getDouble("kwh");
        this.customerId = jsonObject.getLong("customerId");
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getKwh() {
        return kwh;
    }

    public void setKwh(Double kwh) {
        this.kwh = kwh;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    // JSON conversion methods
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("kwh", kwh);
        jsonObject.put("customerId", customerId);
        return jsonObject;
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
