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
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;

import fr.sea_race.client.searace.R;
import fr.sea_race.client.searace.login.ServerCheckout;
import fr.sea_race.client.searace.models.Race;
import fr.sea_race.client.searace.models.Skipper;

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
        loadSkippers(rootView, rootView.getContext());

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
                    final String id = availableRaces.get(i).id;
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            registerRace(v, id);
                        }
                    });
                    availableRaceLayout.addView(button);
                    Log.i("Available race", availableRaces.get(i).name);
                }
            }

        }).execute();

    }

    public void loadSkippers (final View view, final Context context) {

        (new AsyncTask<String, List<Skipper>, List<Skipper>>() {

            @Override
            protected List<Skipper> doInBackground(String... account) {
                return Skipper.query();
            }

            @Override
            protected void onPostExecute(List<Skipper> skippers) {
                LinearLayout currentRaceLayout = (LinearLayout) view.findViewById(R.id.dashboard_skipper_races);
                currentRaceLayout.removeAllViews();
                for (int i=0; i<skippers.size(); i++) {
                    Button button = new Button(context);
                    button.setText(skippers.get(i).race.name);
                    final String id = skippers.get(i).id;
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            accessSkipper(v, id);
                        }
                    });
                    currentRaceLayout.addView(button);
                    Log.i("Current race", skippers.get(i).race.name);
                }
            }

        }).execute();

    }

    private void accessSkipper(View v, String id) {
        Log.i("Access skipper", id);
    }


    private void registerRace (View v, String id) {
        Log.i("Register race", id);
    }

}
