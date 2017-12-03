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
import android.widget.Button;
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

        loadAvailableRaces(rootView, rootView.getContext());
        loadCurrentRaces(rootView, rootView.getContext());

        return rootView;
    }

    public void loadAvailableRaces (final View view, final Context context) {

        (new AsyncTask<String, List<Race>, List<Race>>() {

            @Override
            protected List<Race> doInBackground(String... account) {
                return Race.getAvailable();
            }

            @Override
            protected void onPostExecute(List<Race> availableRaces) {
                LinearLayout availableRaceLayout = (LinearLayout) view.findViewById(R.id.dashboard_available_races);
                availableRaceLayout.removeAllViews();
                for (int i=0; i<availableRaces.size(); i++) {
                    Button button = new Button(context);
                    button.setText(availableRaces.get(i).name);
                    availableRaceLayout.addView(button);
                    Log.i("Available race", availableRaces.get(i).name);
                }
            }

        }).execute();

    }

    public void loadCurrentRaces (final View view, final Context context) {

        (new AsyncTask<String, List<Race>, List<Race>>() {

            @Override
            protected List<Race> doInBackground(String... account) {
                return Race.getOnGoing();
            }

            @Override
            protected void onPostExecute(List<Race> currentRaces) {
                LinearLayout currentRaceLayout = (LinearLayout) view.findViewById(R.id.dashboard_skipper_races);
                currentRaceLayout.removeAllViews();
                for (int i=0; i<currentRaces.size(); i++) {
                    Button button = new Button(context);
                    button.setText(currentRaces.get(i).name);
                    currentRaceLayout.addView(button);
                    Log.i("Current race", currentRaces.get(i).name);
                }
            }

        }).execute();

    }


    private void registerRace (String id) {

    }


}
