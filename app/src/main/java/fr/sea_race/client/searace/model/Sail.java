package fr.sea_race.client.searace.model;

import android.app.Fragment;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fr.sea_race.client.searace.R;

/**
 * Created by cyrille on 08/12/17.
 */

public class Sail {
    public String id;
    public String name;
    public Boat boat;

    private static Map<String, String> translations = new HashMap<String, String>();


    public static void loadTranslations(Context context) {
        if (translations.isEmpty()) {
            translations.put("foc", context.getString(R.string.sail_label_foc));
            translations.put("spi", context.getString(R.string.sail_label_spi));
            translations.put("foc2", context.getString(R.string.sail_label_foc2));
            translations.put("genois", context.getString(R.string.sail_label_genois));
            translations.put("code zéro", context.getString(R.string.sail_label_code_zero));
            translations.put("spi léger", context.getString(R.string.sail_label_spi_leger));
            translations.put("gennaker", context.getString(R.string.sail_label_gennaker));
        }
    }

    public Sail(JSONObject data) throws JSONException {
        this.id = data.has("id") ? data.getString("id") : "";
        name = data.has("name") ? data.getString("name") : "";
        boat = data.has("allowedBoat") ? new Boat(data.getJSONObject("allowedBoat")) : null;
    }

    public boolean equals(Sail other) {
        if (other == null) {
            return false;
        }
        return id.equals(other.id);
    }

    @Override
    public String toString() {
        String key = this.name.replaceAll("[^\\-]*\\-", "");
        return translations.get(key);
    }
}
