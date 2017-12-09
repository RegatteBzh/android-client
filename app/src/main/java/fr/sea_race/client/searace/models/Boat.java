package fr.sea_race.client.searace.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cyrille on 08/12/17.
 */

public class Boat {
    public String id;
    public String name;

    public Boat(JSONObject data) throws JSONException {
        this.id = data.has("id") ? data.getString("id") : "";
        this.name = data.has("name") ? data.getString("name") : "";
    }
}
