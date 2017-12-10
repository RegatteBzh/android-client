package fr.sea_race.client.searace.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cyrille on 08/12/17.
 */

public class Player {
    public String id;
    public String email;
    public String name;
    public boolean isAdmin;

    public Player(JSONObject data) throws JSONException {
        this.id = data.has("id") ? data.getString("id") : "";
        this.email = data.has("email") ? data.getString("email") : "";
        this.name = data.has("name") ? data.getString("name") : "";
        this.isAdmin = data.has("admin") ? data.getBoolean("admin") : false;
    }
}
