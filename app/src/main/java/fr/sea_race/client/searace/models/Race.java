package fr.sea_race.client.searace.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.sea_race.client.searace.net.ApiRequest;
import fr.sea_race.client.searace.net.BadRequestException;

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

    public static List<Race> getAvailable() {
        List<Race> result = new ArrayList<Race>();
        try {
            String response = ApiRequest.get("races/available", null);
            return buildFromString(response);
        } catch (BadRequestException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Race> getOnGoing() {
        List<Race> result = new ArrayList<Race>();
        try {
            String response = ApiRequest.get("races/", null);
            return buildFromString(response);
        } catch (BadRequestException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Skipper register() {
        try {
            Map<String, String> query = new HashMap<String, String>();
            query.put("id", this.id);
            String response = ApiRequest.get("races/register/:id", query);
            return new Skipper(new JSONObject(response));
        } catch (BadRequestException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Race> buildFromString(String serverResponse) {
        List<Race> result = new ArrayList<Race>();
        try {
            JSONArray jsonRaces = new JSONArray(serverResponse);
            for (int i = 0; i< jsonRaces.length(); i++) {
                Race race = new Race(jsonRaces.getJSONObject(i));
                if (race != null) {
                    result.add(race);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }




}
