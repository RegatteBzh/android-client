package fr.sea_race.client.searace.models.wind;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by cyrille on 09/12/17.
 */

public class WindHeader {
    public double dx;
    public double dy;
    public double la1;
    public double lo1;
    public double la2;
    public double lo2;
    public int nx;
    public int ny;
    public int parameterCategory;
    public int parameterNumber;
    public String parameterNumberName;
    public String parameterUnit;
    public Date refTime;

    public WindHeader (JSONObject data) throws JSONException {
        this.dx = data.has("dx") ? data.getDouble("dx") : 0;
        this.dy = data.has("dy") ? data.getDouble("dy") : 0;
        this.la1 = data.has("la1") ? data.getDouble("la1") : 0;
        this.la2 = data.has("la2") ? data.getDouble("la2") : 0;
        this.lo1 = data.has("lo1") ? data.getDouble("lo1") : 0;
        this.lo2 = data.has("lo2") ? data.getDouble("lo2") : 0;
        this.nx = data.has("nx") ? data.getInt("nx") : 0;
        this.ny = data.has("ny") ? data.getInt("ny") : 0;
        this.parameterCategory = data.has("parameterCategory") ? data.getInt("parameterCategory") : 0;
        this.parameterNumber = data.has("parameterNumber") ? data.getInt("parameterNumber") : 0;

        this.parameterNumberName = data.has("parameterNumberName") ? data.getString("parameterNumberName") : "";
        this.parameterUnit = data.has("parameterUnit") ? data.getString("parameterUnit") : "";
        this.refTime = data.has("refTime") ? new Date(data.getInt("refTime") * 1000) : new Date();
    }
}
