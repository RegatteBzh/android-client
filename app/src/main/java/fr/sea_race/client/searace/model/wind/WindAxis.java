package fr.sea_race.client.searace.model.wind;

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

    public float data[][];
    public WindHeader header;

    public WindAxis(JSONObject data) throws JSONException {
        this.header = data.has("header") ? new WindHeader(data.getJSONObject("header")) : null;
        this.data = new float[this.header.nx][this.header.ny];

        if (data.has("data")) {
            JSONArray dataLst = data.getJSONArray("data");
            for (int i=0; i<dataLst.length(); i++) {
                this.data[i % this.header.nx][i / this.header.nx] = (float)dataLst.getDouble(i);
            }
        }
    }

    public float getWindAt(LatLng position) {
        int x = (int)Math.round(((position.longitude + 360) % 360) / this.header.dx);
        int y = (int)Math.round((this.header.la1 - position.latitude) / this.header.dy);
        return this.data[x][y];
    }
}
