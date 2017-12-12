package fr.sea_race.client.searace.component;

import fr.sea_race.client.searace.model.Sail;

/**
 * Created by cyrille on 11/12/17.
 */

public interface OnSailChangeListener {
    /**
     * Event thrown when sail has changed
     * @param sail New Sail
     */
    public void onChange(Sail sail);
}
