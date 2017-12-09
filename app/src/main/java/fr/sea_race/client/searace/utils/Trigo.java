package fr.sea_race.client.searace.utils;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by cyrille on 09/12/17.
 */

public class Trigo {

    public static double EarthRadius = 6371;

    public static double meterPerSecondToKnot(double speed) {
        return speed * 1.943844492;
    }

    public static double knotToMeterPerSecond(double speed) {
        return speed / 1.943844492;
    }

    public static double interpolation (double x, double y, double g00, double g10, double g01, double g11) {
        double rx = (1 - x);
        double ry = (1 - y);
        double a = rx * ry;
        double b = x * ry;
        double c = rx * y;
        double d = x * y;
        return g00 * a + g10 * b + g01 * c + g11 * d;
    }

    // http://www.movable-type.co.uk/scripts/latlong.html#destPoint
    public static LatLng pointAtDistanceAndBearing(LatLng from, double distKm, double bearingDegree) {
        double dr = distKm / EarthRadius;
        double bearingRad = (bearingDegree * (Math.PI / 180));

        double latFrom = (from.latitude * (Math.PI / 180));
        double lngFrom = (from.longitude * (Math.PI / 180));

        double latTo = Math.asin(Math.sin(latFrom) * Math.cos(dr) + Math.cos(latFrom) * Math.sin(dr) * Math.cos(bearingRad));
        double lngTo = lngFrom + Math.atan2(
                Math.sin(bearingRad) * Math.sin(dr) * Math.cos(latFrom),
                Math.cos(dr) - (Math.sin(latFrom) * Math.sin(latTo))
        );

        lngTo = ( (lngTo + 3 * Math.PI) % (2 * Math.PI) ) - Math.PI;

        return new LatLng(latTo * 180 / Math.PI, lngTo * 180 / Math.PI);
    }
}
