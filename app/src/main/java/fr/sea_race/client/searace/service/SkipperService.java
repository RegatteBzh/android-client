package fr.sea_race.client.searace.service;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import fr.sea_race.client.searace.R;
import fr.sea_race.client.searace.model.Skipper;
import fr.sea_race.client.searace.net.ApiRequest;
import fr.sea_race.client.searace.task.Poller;
import fr.sea_race.client.searace.task.TaskPoller;
import fr.sea_race.client.searace.task.TaskReport;

/**
 * Created by cyrille on 10/12/17.
 */

public class SkipperService {

    public static void loadSkipper(String skipperId, final TaskReport<Skipper> task) {
        Map<String, String> query = new HashMap<String, String>();
        query.put("id", skipperId);
        AsyncHttpClient client = ApiRequest.client();
        client.get( ApiRequest.url("skippers/:id/", query), new JsonHttpResponseHandler() {
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
    }

    public static void setBearing(Skipper skipper, float bearing, final TaskReport<Skipper> task) {
        Map<String, String> query = new HashMap<String, String>();
        query.put("id", skipper.id);

        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("bearing", (int)Math.floor(bearing + 0.5d));
            StringEntity entity = new StringEntity(jsonParams.toString());

            AsyncHttpClient client = ApiRequest.client();
            client.post(null, ApiRequest.url("skippers/:id/bearing", query), entity, "application/json", new JsonHttpResponseHandler() {
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

    public static void loadSkippers (final TaskReport<List<Skipper>> task) {
        AsyncHttpClient client = ApiRequest.client();
        client.get( ApiRequest.url("skippers/"), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    task.onSuccess(Skipper.fromJsonArray(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                    task.onFailure("JSON error");
                } finally {
                    task.onComplete();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String text, Throwable throwable) {
                Log.i("HTTP ERROR", text);
                task.onFailure(text);
                task.onComplete();
            }
        });
    }

    public static Poller startPoller(final String skipperId, final TaskReport<Skipper> task) {
        return new Poller(5000, 0, new TaskPoller() {
            @Override
            public void tick() {
                loadSkipper(skipperId, task);
            }
        });
    }

}
