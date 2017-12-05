package fr.sea_race.client.searace.main;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import fr.sea_race.client.searace.R;
import fr.sea_race.client.searace.models.Race;
import fr.sea_race.client.searace.models.Skipper;
import fr.sea_race.client.searace.net.ApiRequest;

/**
 * Created by cyrille on 03/12/17.
 */

public class DashboardFragment extends Fragment {

    private Context currentContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_dashboard, container, false);

        currentContext = rootView.getContext();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadAvailableRaces(getView(), currentContext);
        loadSkippers(getView(), currentContext);
    }

    public void loadAvailableRaces (final View view, final Context context) {

        AsyncHttpClient client = ApiRequest.client();
        client.get( ApiRequest.url("races/available"), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    List<Race> availableRaces = Race.fromJsonArray(response);
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

                } catch (JSONException e) {
                    Toast.makeText(currentContext, getString(R.string.http_fail), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String text, Throwable throwable) {
                Toast.makeText(currentContext, getString(R.string.http_fail), Toast.LENGTH_LONG).show();
                Log.i("HTTP ERROR", text);
            }
        });

    }

    public void loadSkippers (final View view, final Context context) {

        AsyncHttpClient client = ApiRequest.client();
        client.get( ApiRequest.url("skippers/"), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    List<Skipper> skippers = Skipper.fromJsonArray(response);
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
                } catch (JSONException e) {
                    Toast.makeText(currentContext, getString(R.string.http_fail), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String text, Throwable throwable) {
                Toast.makeText(currentContext, getString(R.string.http_fail), Toast.LENGTH_LONG).show();
                Log.i("HTTP ERROR", text);
            }
        });

    }

    private void accessSkipper(View v, String id) {
        Log.i("Access skipper", id);
        Bundle bundle = new Bundle();
        bundle.putString("skipperId", id);
        SkipperFragment fragment = new SkipperFragment();
        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }


    private void registerRace (final View v, String id) {
        Log.i("Register race", id);
        Map<String, String> query = new HashMap<String, String>();
        query.put("id", id);
        AsyncHttpClient client = ApiRequest.client();
        client.get( ApiRequest.url("races/register/:id", query), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Skipper skipper = new Skipper(response);
                    Log.i("Skipper", skipper.id);
                    accessSkipper(v, skipper.id);
                } catch (JSONException e) {
                    Toast.makeText(currentContext, getString(R.string.http_fail), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String text, Throwable throwable) {
                Toast.makeText(currentContext, getString(R.string.http_fail), Toast.LENGTH_LONG).show();
                Log.i("HTTP ERROR", text);
            }
        });

    }

}
