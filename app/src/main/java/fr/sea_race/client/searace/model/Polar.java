package fr.sea_race.client.searace.model;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import fr.sea_race.client.searace.utils.Trigo;

/**
 * Created by cyrille on 09/12/17.
 */

public class Polar {
    private List<double[]> data;

    public  Polar(JSONArray data) throws JSONException {
        for (int i = 0; i< data.length(); i++) {
            JSONArray jsonLine = data.getJSONArray(i);
            double line[] = new double[jsonLine.length()];
            for(int j=0; j<jsonLine.length(); j++) {
                line[j] = jsonLine.getDouble(j);
            }
            this.data.add(line);
        }
    }

    public double getSpeedCoefAt(double wind, double direction) {
        double sampledWind0 = Math.floor(wind / 10);
        if (this.data.size() < sampledWind0 + 1) {
            sampledWind0 = this.data.size() - 1;
        }
        double sampledWind1 = sampledWind0 + 1;
        if (this.data.size() < sampledWind1 + 1) {
            sampledWind1 = sampledWind0;
        }
        double sampledDirection0 = Math.floor(direction / 5) % 180;
        double sampledDirection1 = Math.ceil((direction + 5) / 5) % 180;
        return Trigo.interpolation(
            wind / 10 - Math.floor(wind / 10),
            (direction % 180) / 5 - sampledDirection0,
            this.data.get((int)sampledWind0)[(int)sampledDirection0],
            this.data.get((int)sampledWind1)[(int)sampledDirection0],
            this.data.get((int)sampledWind0)[(int)sampledDirection1],
            this.data.get((int)sampledWind1)[(int)sampledDirection1]
        );
    }

}
