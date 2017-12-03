package fr.sea_race.client.searace.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cyrille on 03/12/17.
 */

public class Skipper {
    public String id;
    public Race race;

    public Skipper(String id, Race race) {
        this.id = id;
        this.race = race;
    }

    public Skipper(JSONObject data) throws JSONException {
        this.id = data.has("id") ? data.getString("id") : "";
        this.race = data.has("race") ? new Race(data.getJSONObject("race")) : null;
    }
}
