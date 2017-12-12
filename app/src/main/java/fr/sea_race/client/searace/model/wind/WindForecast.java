package fr.sea_race.client.searace.model.wind;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import fr.sea_race.client.searace.model.ForecastSkipper;
import fr.sea_race.client.searace.model.Polar;
import fr.sea_race.client.searace.model.Skipper;
import fr.sea_race.client.searace.net.ApiRequest;
import fr.sea_race.client.searace.utils.Trigo;

/**
 * Created by cyrille on 09/12/17.
 */

public class WindForecast {

    public Map<Integer, WindMap> data = new HashMap<Integer, WindMap>();
    public static int stepHour = 6;
    public static int stepCount = 4;

    public WindForecast() {

    }

    public WindSpeed getWindAt(LatLng position, int index) {
        if (data.containsKey(index)) {
            return data.get(index).getWindAt(position);
        }
        return null;
    }

    public ForecastSkipper forecast(Skipper skipper, Polar polar) throws Exception {
        ForecastSkipper result = new ForecastSkipper();
        result.way.add(skipper.position);
        for (int i = 0; i < this.stepCount; i++) {
        LatLng lastPos = result.getLastPosition();
        WindSpeed windSpeedKnot = this.getWindAt(lastPos, i);

        double relativeWindBearing = skipper.getRelativeAngle(windSpeedKnot.bearing());

        double speedKnot = windSpeedKnot.value() * polar.getSpeedCoefAt(windSpeedKnot.value(), relativeWindBearing);
        double distanceKm = Trigo.knotToMeterPerSecond(speedKnot) * 3.6 * stepHour;
            result.speed.add(speedKnot);
            result.way.add(Trigo.pointAtDistanceAndBearing(lastPos, distanceKm, skipper.direction));
            result.windRelativeBearings.add(relativeWindBearing);
            result.windBearing.add(windSpeedKnot.bearing());
            result.windSpeedKnot.add(windSpeedKnot.value());
            result.distanceKm.add(distanceKm);
        }
        return result;
    }
}
