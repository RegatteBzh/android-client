package fr.sea_race.client.searace.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cyrille on 08/12/17.
 */

public class Sail {
    public String id;

    public Sail(JSONObject data) throws JSONException {
        this.id = data.has("id") ? data.getString("id") : "";
    }
}
