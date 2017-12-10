package fr.sea_race.client.searace.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by cyrille on 09/12/17.
 */

public class ForecastSkipper {

    public List<LatLng> way;
    public List<Double> speed;
    public List<Double> windSpeedKnot;
    public List<Double> windRelativeBearings;
    public List<Double> windBearing;
    public List<Double> distanceKm;


    public double getFirstSpeed () throws Exception {
        if (this.speed == null || this.speed.size() == 0) {
            throw new Exception("Empty forecast");
        }
        return this.speed.get(0);
    }

    public LatLng getLastPosition () throws Exception {
        if (this.way== null || this.way.size() == 0) {
            throw new Exception("Empty forecast");
        }
        return this.way.get(this.way.size()-1);
    }

    public double getFirstWindDirection () throws Exception {
        if (this.windRelativeBearings == null || this.windRelativeBearings.size() == 0) {
            throw new Exception("Empty forecast");
        }
        return this.windRelativeBearings.get(0);
    }
}
