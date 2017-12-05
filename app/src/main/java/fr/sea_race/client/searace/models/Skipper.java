package fr.sea_race.client.searace.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cyrille on 03/12/17.
 */

public class Skipper {
    public String id;
    public Race race;
    public double speed;

    public Skipper() {
    }

    public Skipper(JSONObject data) throws JSONException {
        this.id = data.has("id") ? data.getString("id") : "";
        this.race = data.has("race") ? new Race(data.getJSONObject("race")) : null;
        this.speed = data.has("speed") ? data.getDouble("speed") : 0;
    }

    public static List<Skipper> fromJsonArray(JSONArray jsonSkippers) throws JSONException {
        List<Skipper> result = new ArrayList<Skipper>();
        for (int i = 0; i< jsonSkippers.length(); i++) {
            Skipper skipper = new Skipper(jsonSkippers.getJSONObject(i));
            if (skipper != null) {
                result.add(skipper);
            }
        }
        return result;
    }
}
