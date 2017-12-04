package fr.sea_race.client.searace.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.sea_race.client.searace.net.ApiRequest;
import fr.sea_race.client.searace.net.BadRequestException;

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

    public static List<Skipper> query() {
        List<Skipper> result = new ArrayList<Skipper>();
        try {
            String response = ApiRequest.get("skippers/", null);
            return arrayFromString(response);
        } catch (BadRequestException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static List<Skipper> arrayFromString(String serverResponse) {
        List<Skipper> result = new ArrayList<Skipper>();
        try {
            JSONArray jsonSkippers = new JSONArray(serverResponse);
            for (int i = 0; i< jsonSkippers.length(); i++) {
                Skipper skipper = new Skipper(jsonSkippers.getJSONObject(i));
                if (skipper != null) {
                    result.add(skipper);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
