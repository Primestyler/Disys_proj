package DataCollectionReceiver.DataCollectionReceiver.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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

    public String getDbUrl() {
        return dbUrl;
    }

    public static ChargeData fromJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        String dbUrl = jsonObject.getString("dbUrl");

        List<Charge> charges = new ArrayList<>();
        JSONArray jsonCharges = jsonObject.getJSONArray("charges");
        for (int i = 0; i < jsonCharges.length(); i++) {
            JSONObject jsonCharge = jsonCharges.getJSONObject(i);
            Charge charge = new Charge(jsonCharge);
            charges.add(charge);
        }

        return new ChargeData(charges, dbUrl);
    }
}
