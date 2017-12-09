package fr.sea_race.client.searace.models.wind;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyrille on 09/12/17.
 */

public class WindMap {
    private List<WindAxis> data;

    public WindMap(JSONArray data) throws JSONException {
        this.data = new ArrayList<WindAxis>();
        for (int i = 0; i< data.length(); i++) {
            this.data.add(new WindAxis(data.getJSONObject(i)));
        }
    }

    public WindAxis getU() {
        for (int i = 0; i<this.data.size(); i++) {
            if (this.data.get(i).header.parameterNumber == 2) {
                return this.data.get(i);
            }
        }
        return null;
    }

    public WindAxis getV() {
        for (int i = 0; i<this.data.size(); i++) {
            if (this.data.get(i).header.parameterNumber == 3) {
                return this.data.get(i);
            }
        }
        return null;
    }

    public WindSpeed getWindAt(LatLng position) {
        if (this.data.size() != 2) {
            return null;
        }
        WindAxis u = this.getU();
        WindAxis v = this.getV();

        if (u == null || v == null) {
            return null;
        }

        return new WindSpeed(u.getWindAt(position), v.getWindAt(position));
    }

}
