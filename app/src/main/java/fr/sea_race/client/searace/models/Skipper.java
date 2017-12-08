package fr.sea_race.client.searace.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by cyrille on 03/12/17.
 */

public class Skipper {
    public String id;
    public Race race;
    public Player player;
    public Boat boat;
    public Sail sail;
    public double speed;
    public double direction;
    public boolean groundFail;
    public boolean sailDown;
    public int rank;
    public double distanceToFinish;
    public double windAngle;
    public double windRelativeAngle;
    public double windSpeed;
    public Date finished;
    public boolean hasFinished = false;


    public Skipper() {
    }

    public Skipper(JSONObject data) throws JSONException {
        this.id = data.has("id") ? data.getString("id") : "";
        this.race = data.has("race") ? new Race(data.getJSONObject("race")) : null;
        this.player = data.has("player") ? new Player(data.getJSONObject("player")) : null;
        this.boat = data.has("boat") ? new Boat(data.getJSONObject("boat")) : null;
        this.sail = data.has("sail") ? new Sail(data.getJSONObject("sail")) : null;
        this.speed = data.has("speed") ? data.getDouble("speed") : 0;
        this.direction = data.has("direction") ? data.getDouble("direction") : 0f;
        this.groundFail = data.has("groundFail") ? data.getBoolean("groundFail") : false;
        this.sailDown = data.has("sailDown") ? data.getBoolean("sailDown") : false;
        this.rank = data.has("rank") ? data.getInt("rank") : -1;
        this.distanceToFinish = data.has("distanceToFinish") ? data.getDouble("distanceToFinish") : 0d;
        this.windAngle = data.has("windAngle") ? data.getDouble("windAngle") : 0d;
        this.windRelativeAngle = data.has("windRelativeAngle") ? data.getDouble("windRelativeAngle") : 0d;
        this.windSpeed = data.has("windSpeed") ? data.getDouble("windSpeed") : 0d;
        long lFinished = data.has("finished") ? data.getLong("finished") : -1;
        if (lFinished >0) {
            this.finished = new Date(lFinished * 1000);
            this.hasFinished = true;
        }
    }

    public static List<Skipper> fromJsonArray(JSONArray jsonSkippers) throws JSONException {
        List<Skipper> result = new ArrayList<Skipper>();
        for (int i = 0; i< jsonSkippers.length(); i++) {
            Skipper skipper = new Skipper(jsonSkippers.getJSONObject(i));
            if (skipper != null) {
                result.add(skipper);
            }
        }
        return result;
    }
}
