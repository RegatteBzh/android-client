package fr.sea_race.client.searace.models.wind;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyrille on 09/12/17.
 */

public class WindAxis {

    public List<Double> data;
    public WindHeader header;

    public WindAxis(JSONObject data) throws JSONException {
        this.header = data.has("header") ? new WindHeader(data.getJSONObject("header")) : null;
        if (data.has("data")) {
            this.data = new ArrayList<Double>();
            JSONArray dataLst = data.getJSONArray("data");
            for (int i=0; i<dataLst.length(); i++) {
                double elt = dataLst.getDouble(i);
                this.data.add(elt);
            }
        }
    }

    public double getWindAt(LatLng position) {
        int x = (int)Math.round(((position.longitude + 360) % 360) / this.header.dx);
        int y = (int)Math.round((this.header.la1 - position.latitude) / this.header.dy);
        return  this.data.get(y * this.header.nx + x);
    }
}
