package DataCollectionDispacher.DataCollectionDispacher.model;

public class StationMessage {
    private String customerId;
    private Long stationId;
    private String dbUrl;

    public StationMessage(String customerId, Long stationId, String dbUrl) {
        this.customerId = customerId;
        this.stationId = stationId;
        this.dbUrl = dbUrl;
    }

    // Getters and setters
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    @Override
    public String toString() {
        return "StationMessage{" +
                "customerId='" + customerId + '\'' +
                ", stationId=" + stationId +
                ", dbUrl='" + dbUrl + '\'' +
                '}';
    }
}
