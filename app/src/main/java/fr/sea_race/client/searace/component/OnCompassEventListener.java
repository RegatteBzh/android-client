package fr.sea_race.client.searace.component;

/**
 * Created by cmeichel on 12/8/17.
 */

public interface OnCompassEventListener {
    public void onAngleUpdate(float angle);
    public void onStartAngle(float angle);
    public void onProcessAngle(float angle);
}
