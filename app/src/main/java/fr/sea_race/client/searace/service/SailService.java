package fr.sea_race.client.searace.service;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import fr.sea_race.client.searace.model.Boat;
import fr.sea_race.client.searace.model.Polar;
import fr.sea_race.client.searace.model.Sail;
import fr.sea_race.client.searace.model.Skipper;
import fr.sea_race.client.searace.model.wind.WindForecast;
import fr.sea_race.client.searace.model.wind.WindMap;
import fr.sea_race.client.searace.net.ApiRequest;
import fr.sea_race.client.searace.task.TaskReport;

/**
 * Created by cyrille on 10/12/17.
 */

public class SailService {
    public static void loadPolar(Sail sail, final TaskReport<Polar> task) {
        AsyncHttpClient client = ApiRequest.assets();
        client.get( ApiRequest.assets(String.format("polars/%s.json", sail.id)), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    task.onSuccess(new Polar(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                    task.onFailure("bad json");
                } finally {
                    task.onComplete();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String text, Throwable throwable) {
                Log.i("HTTP ERROR", text);
                task.onFailure(text);
            }
        });
    }

    public static void loadSails(Boat boat, final TaskReport<List<Sail>> task) {
        Map<String, String> query = new HashMap<String, String>();
        query.put("id", boat.id);
        AsyncHttpClient client = ApiRequest.client();
        client.get( ApiRequest.url("boats/:id/sails/", query), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                List<Sail> sails = new ArrayList<Sail>();
                try {
                    for(int i=0; i<response.length(); i++) {
                        sails.add(new Sail(response.getJSONObject(i)));
                    }
                    task.onSuccess(sails);
                } catch (JSONException e) {
                    e.printStackTrace();
                    task.onFailure("bad json");
                } finally {
                    task.onComplete();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String text, Throwable throwable) {
                Log.i("HTTP ERROR", text);
                task.onFailure(text);
            }
        });
    }

    public static void setSail(Skipper skipper,Sail sail, final TaskReport<Skipper> task) {
        Map<String, String> query = new HashMap<String, String>();
        query.put("id", skipper.id);

        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("sailId", sail.id);
            StringEntity entity = new StringEntity(jsonParams.toString());

            AsyncHttpClient client = ApiRequest.client();
            client.post(null, ApiRequest.url("skippers/:id/sail", query), entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        task.onSuccess(new Skipper(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        task.onFailure("JSON error");
                    } finally {
                        task.onComplete();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                    Log.i("HTTP ERROR", response.toString());
                    task.onFailure(response.toString());
                    task.onComplete();
                }

            });
        } catch (JSONException e) {
            e.printStackTrace();
            task.onFailure(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            task.onFailure(e.getMessage());
        }
    }
}
