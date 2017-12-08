package fr.sea_race.client.searace.main;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import fr.sea_race.client.searace.R;
import fr.sea_race.client.searace.component.Compass;
import fr.sea_race.client.searace.component.OnCompassEventListener;
import fr.sea_race.client.searace.models.Skipper;
import fr.sea_race.client.searace.net.ApiRequest;

/**
 * Created by cmeichel on 12/5/17.
 */

public class SkipperFragment extends Fragment {

    private Context currentContext;
    private String skipperId;
    private Skipper skipper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            skipperId = bundle.getString("skipperId", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_skipper, container, false);

        currentContext = rootView.getContext();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadSkipper();
        manageCompass();
    }

    private void manageCompass() {
        Compass compass = (Compass)getView().findViewById(R.id.compass);
        compass.setOnCompassEventListener(new OnCompassEventListener() {
            @Override
            public void OnAngleUpdate(float angle) {
                skipper.direction = angle;
                TextView direction = (TextView)getView().findViewById(R.id.skipper_direction_value);
                direction.setText(String.format("%.0f", skipper.direction));
            }

            @Override
            public void OnStartAngle(float angle) {

            }
        });
    }

    private void loadSkipper() {
        Map<String, String> query = new HashMap<String, String>();
        query.put("id", skipperId);
        AsyncHttpClient client = ApiRequest.client();
        client.get( ApiRequest.url("skippers/:id/", query), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    skipper = new Skipper(response);
                    Log.i("Skipper data", skipper.id);
                    updateView();
                } catch (JSONException e) {
                    Toast.makeText(currentContext, getString(R.string.skipper_fail), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String text, Throwable throwable) {
                Toast.makeText(currentContext, getString(R.string.skipper_fail), Toast.LENGTH_LONG).show();
                Log.i("HTTP ERROR", text);
            }
        });
    }

    private void updateView() {
        TextView speed = (TextView)getView().findViewById(R.id.skipper_speed_value);
        speed.setText(String.format("%.2f", skipper.speed));

        TextView direction = (TextView)getView().findViewById(R.id.skipper_direction_value);
        direction.setText(String.format("%.0f", skipper.direction));

        Compass compass = (Compass)getView().findViewById(R.id.compass);
        compass.setAngle((float)skipper.direction);

    }
}
