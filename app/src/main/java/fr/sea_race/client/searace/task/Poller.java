package fr.sea_race.client.searace.task;

import android.os.Handler;
import android.util.Log;

/**
 * Created by cyrille on 10/12/17.
 */

public class Poller {
    private Handler mHandler;
    private Runnable mRunnable;
    public boolean running = false;

    public Poller(final int intervalMilliseconds, final int startMilliseconds, final TaskPoller task) {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                task.tick();
                mHandler.postDelayed(this,intervalMilliseconds);
            }
        };
        mHandler = new Handler();
        start(startMilliseconds);
    }

    public void stop () {

        if(mHandler != null && running) {
            mHandler.removeCallbacks(mRunnable);
            running = false;
        }
    }

    public void start (int startMilliseconds) {
        if(mHandler != null && !running) {
            Log.i("POLLER", "Will start in " + startMilliseconds);
            mHandler.postDelayed(mRunnable, startMilliseconds);
            running = true;
        }
    }
}
