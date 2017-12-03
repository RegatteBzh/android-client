package fr.sea_race.client.searace.main;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;

import fr.sea_race.client.searace.R;
import fr.sea_race.client.searace.login.ServerCheckout;
import fr.sea_race.client.searace.models.Race;

import static fr.sea_race.client.searace.models.Race.getAvailable;

/**
 * Created by cyrille on 03/12/17.
 */

public class DashboardFragment extends Fragment {

    private Context currentContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dashboard_main, container, false);

        currentContext = rootView.getContext();

        loadRaces(rootView);

        return rootView;
    }

    public void loadRaces (View view) {

        final LinearLayout availableRaceLayout = (LinearLayout) view.findViewById(R.id.dashboard_available_races);
        availableRaceLayout.removeAllViews();

        final LinearLayout currentRaceLayout = (LinearLayout) view.findViewById(R.id.dashboard_skipper_races);
        currentRaceLayout.removeAllViews();

        (new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... account) {
                List<Race> availableRaces = Race.getAvailable();
                for (int i=0; i<availableRaces.size(); i++) {
                    /*TextView textView = new TextView(currentContext);
                    textView.setText(availableRaces.get(i).name);
                    availableRaceLayout.addView(textView);*/
                    Log.i("Available races", availableRaces.get(i).name);
                }

                List<Race> currentRaces = Race.getOnGoing();
                for (int i=0; i<currentRaces.size(); i++) {
                    /*TextView textView = new TextView(currentContext);
                    textView.setText(currentRaces.get(i).name);
                    currentRaceLayout.addView(textView);*/
                    Log.i("Current races", currentRaces.get(i).name);
                }
                return null;
            }
        }).execute();


    }


}
