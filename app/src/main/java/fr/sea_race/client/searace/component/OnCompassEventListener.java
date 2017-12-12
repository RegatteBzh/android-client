package fr.sea_race.client.searace.component;

/**
 * Created by cmeichel on 12/8/17.
 */

public interface OnCompassEventListener {
    /**
     * Event thrown when edition is finished
     * @param angle New angle
     */
    public void onAngleUpdate(float angle);

    /**
     * Event thrown when beginning to edit angle
     * @param angle Initial angle
     */
    public void onStartAngle(float angle);

    /**
     * Event thrown while moving angle on the compass
     * @param angle Temporary angle
     */
    public void onProcessAngle(float angle);
}
