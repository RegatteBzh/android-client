package fr.sea_race.client.searace.models;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cyrille on 03/12/17.
 */

public class Race {

    public String id;
    public String name;
    public String description;
    public Boat allowedBoat;
    public int endRayKm;
    public Date dateStart;
    public Date dateEnd;

    public LatLng start;
    public LatLng end;

    public boolean hasDateStart;
    public boolean hasDateEend;

    public Race(JSONObject data) throws JSONException {
        id = data.has("id") ? data.getString("id") : "";
        name = data.has("name") ? data.getString("name") : "";
        description = data.has("description") ? data.getString("description") : "";
        allowedBoat = data.has("allowedBoat") ? new Boat(data.getJSONObject("allowedBoat")) : null;
        endRayKm = data.has("endRayKm") ? data.getInt("endRayKm") : 0;

        long lDateStart = data.has("dateStart") ? data.getLong("dateStart") : -1;
        if (lDateStart >0) {
            this.dateStart = new Date(lDateStart * 1000);
            this.hasDateStart = true;
        }

        long lDateEnd = data.has("dateEnd") ? data.getLong("dateEnd") : -1;
        if (lDateStart >0) {
            this.dateEnd = new Date(lDateEnd * 1000);
            this.hasDateEend = true;
        }

        start = data.has("start") ? Model.position(data.getJSONObject("start")) : null;
        end = data.has("end") ? Model.position(data.getJSONObject("end")) : null;
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
