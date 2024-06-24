package StationDataCollector.StationDataCollector.entity;

import java.util.List;

public class ChargeData {
    private List<Charge> charges;
    private String dbUrl;

    public ChargeData(List<Charge> charges, String dbUrl) {
        this.charges = charges;
        this.dbUrl = dbUrl;
    }

    public List<Charge> getCharges() {
        return charges;
    }

    public void setCharges(List<Charge> charges) {
        this.charges = charges;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    @Override
    public String toString() {
        return "ChargeData{" +
                "charges=" + charges +
                ", dbUrl='" + dbUrl + '\'' +
                '}';
    }
}
