package fr.sea_race.client.searace.net;

import com.loopj.android.http.AsyncHttpClient;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by cyrille on 02/12/17.
 */

public class ApiRequest {

    public static String baseUrl = "https://sea-race.fr";
    private static String apiPrefix = "/api";
    private static String assetsPrefix = "/assets";
    public static String token = "";

    public static void setToken(String authToken) {
        token = authToken;
    }

    public static void clearToken() {
        setToken("");
    }

    public static AsyncHttpClient client() {
        AsyncHttpClient newClient = new AsyncHttpClient();
        newClient.addHeader("Authorization", token);
        return newClient;
    }

    public static AsyncHttpClient assets() {
        return new AsyncHttpClient();
    }

    public static String url(String path) {
        return baseUrl + apiPrefix + (path.charAt(0) == '/' ? "" : "/") + path;
    }

    public static String assets(String path) {
        return baseUrl + assetsPrefix + (path.charAt(0) == '/' ? "" : "/") + path;
    }

    public static String url(String path, Map<String, String> query){
        // Query params replacement
        Pattern pattern = Pattern.compile(":([^/]*)");
        StringBuffer output = new StringBuffer();
        Matcher matcher = pattern.matcher(path);
        while (matcher.find()) {
            String key = matcher.group(1);
            if (query!= null && query.containsKey((key))) {
                matcher.appendReplacement(output, query.get(key));
            } else {
                matcher.appendReplacement(output, "{" + key + "}");
            }
        }
        matcher.appendTail(output);

        return  url(output.toString());
    }
}
