package fr.sea_race.client.searace.task;

/**
 * Created by cyrille on 10/12/17.
 */

public class TaskReport<TObject>{

    public void onComplete() {

    }

    public void onFailure(String reason) {
        onComplete();
    }

    public void onSuccess(TObject forecast) {
        onComplete();
    }
}
