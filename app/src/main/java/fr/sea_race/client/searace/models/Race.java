package fr.sea_race.client.searace.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyrille on 03/12/17.
 */

public class Race {

    public String id;
    public String name;
    public String description;

    public Race(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Race(JSONObject data) throws JSONException {
        id = data.has("id") ? data.getString("id") : "";
        name = data.has("name") ? data.getString("name") : "";
        description = data.has("description") ? data.getString("description") : "";
    }

    public static List<Race> fromJsonArray(JSONArray jsonRaces) throws JSONException {
        List<Race> result = new ArrayList<Race>();
        for (int i = 0; i< jsonRaces.length(); i++) {
            Race race = new Race(jsonRaces.getJSONObject(i));
            if (race != null) {
                result.add(race);
            }
        }
        return result;
    }

}
