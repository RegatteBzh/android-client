package fr.sea_race.client.searace.model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cyrille on 09/12/17.
 */

public class Model {
    public static LatLng position(JSONObject data) throws JSONException {
        double latitude = data.has("lat") ? data.getDouble("lat") : 0;
        double longitude = data.has("lng") ? data.getDouble("lng") : 0;
        return new LatLng(latitude, longitude);
    }
}
