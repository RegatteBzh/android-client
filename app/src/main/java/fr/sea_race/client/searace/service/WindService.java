package fr.sea_race.client.searace.service;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import fr.sea_race.client.searace.model.wind.WindForecast;
import fr.sea_race.client.searace.model.wind.WindMap;
import fr.sea_race.client.searace.net.ApiRequest;
import fr.sea_race.client.searace.task.TaskReport;

/**
 * Created by cyrille on 10/12/17.
 */

public class WindService {

    private static String precision = "1.0";

    public static void loadWindMap(final int index, final TaskReport<WindMap> task) {
        AsyncHttpClient client = ApiRequest.assets();
        String filename = ApiRequest.assets(String.format("winds/%s/wind%03d.json", precision, index * WindForecast.stepHour));
        Log.i("WIND", filename);
        client.get( filename, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    WindMap map = new WindMap(response);
                    map.index = index;
                    task.onSuccess(map);
                    task.onComplete();
                } catch (JSONException e) {
                    e.printStackTrace();
                    task.onFailure("bad json");
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

    public static void loadAllForecast(final TaskReport<WindForecast> task) {
        final WindForecast forecast = new WindForecast();
        final List<Boolean> done = new ArrayList<Boolean>();
        for(int i = 0; i< WindForecast.stepCount; i++) {
            final int index = i;
            loadWindMap(i, new TaskReport<WindMap>() {
                @Override
                public void onFailure(String reason) {
                    done.add(false);
                }

                @Override
                public void onSuccess(WindMap map) {
                    forecast.data.put(index, map);
                    done.add(true);
                }

                @Override
                public void onComplete() {
                    if (done.size() != WindForecast.stepCount) {
                        return;
                    }

                    boolean success = true;
                    for( int j=0; j<done.size(); j++) {
                        success &= done.get(j);
                    }

                    if (success) {
                        task.onSuccess(forecast);

                    } else {
                        task.onFailure("Could not load all winds");
                    }
                    task.onComplete();
                }
            });
        }
    }
}
