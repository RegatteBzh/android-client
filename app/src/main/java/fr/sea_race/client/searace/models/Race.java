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

public class Race {

    public String id;
    public String name;
    public String description;

    public Race(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    private static List<Race> buildFromString(String serverResponse) {
        List<Race> result = new ArrayList<Race>();
        try {
            JSONArray jsonRaces = new JSONArray(serverResponse);
            for (int i = 0; i< jsonRaces.length(); i++) {
                JSONObject jsonRace = jsonRaces.getJSONObject(i);
                String id = jsonRace.has("id") ? jsonRace.getString("id") : "";
                String name = jsonRace.has("name") ? jsonRace.getString("name") : "";
                String description = jsonRace.has("description") ? jsonRace.getString("description") : "";
                result.add(new Race(id, name, description));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }


}
