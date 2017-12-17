package fr.sea_race.client.searace.model.wind;

/**
 * Created by cyrille on 09/12/17.
 */

public class WindSpeed {
    public double u;
    public double v;

    public WindSpeed(double u, double v) {
        this.u = u;
        this.v = v;
    }

    public double valueMeterPerSecond () {
        return Math.sqrt(Math.pow(this.u, 2) + Math.pow(this.u, 2));
    }

    public double valueKnot () {
        return valueMeterPerSecond() * 1.9438444924574;
    }

    public double bearing() {
        return (90 - Math.atan2(this.v, this.u) * 180 / Math.PI + 360) % 360;
    }
}
