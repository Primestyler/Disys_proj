package PDFgenerator.PDFgenerator.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChargeData {
    private Map<String, List<Charge>> data;

    public ChargeData(Map<String, List<Charge>> data) {
        this.data = data;
    }

    public Map<String, List<Charge>> getData() {
        return data;
    }

    public static ChargeData fromJson(String json) {
        JSONObject jsonObject = new JSONObject(json);
        Map<String, List<Charge>> chargesMap = new HashMap<>();

        for (String key : jsonObject.keySet()) {
            JSONArray chargesArray = jsonObject.getJSONArray(key);
            List<Charge> charges = new ArrayList<>();
            for (int i = 0; i < chargesArray.length(); i++) {
                charges.add(new Charge(chargesArray.getJSONObject(i)));
            }
            chargesMap.put(key, charges);
        }
        return new ChargeData(chargesMap);
    }

    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, List<Charge>> entry : data.entrySet()) {
            JSONArray chargesArray = new JSONArray();
            for (Charge charge : entry.getValue()) {
                chargesArray.put(charge.toJson());
            }
            jsonObject.put(entry.getKey(), chargesArray);
        }
        return jsonObject.toString();
    }
}
