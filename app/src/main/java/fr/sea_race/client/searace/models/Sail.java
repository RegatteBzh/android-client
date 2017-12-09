package fr.sea_race.client.searace.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cyrille on 08/12/17.
 */

public class Sail {
    public String id;
    public String name;
    public Boat boat;


    public Sail(JSONObject data) throws JSONException {
        this.id = data.has("id") ? data.getString("id") : "";
        name = data.has("name") ? data.getString("name") : "";
        boat = data.has("allowedBoat") ? new Boat(data.getJSONObject("allowedBoat")) : null;
    }
}
